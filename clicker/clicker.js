var fpsMeter;

var game = setGameDefaults();

function init()
{
    fpsMeter = new FPSMeter({ decimals: 0, graph: true, theme: 'colorful', left: '5px' });

    if (localStorage['gameInProgress'])
        restoreState();

    updateDisplayValues();
    UnHideUIElementsOnStartup();

    game._intervalId = setInterval(game.run, 1000 / game.fps);
}

function setGameDefaults()
{
    return {
        // BaseResources
        food : 0, lumber : 0, leather : 0, stone : 0, research : 0,
        // Other Resources
        villagers : 0,
        // Resource Capacities
        foodLimit : 0, lumberLimit : 0, leatherLimit : 0, stoneLimit : 0, researchLimit : 100, villagerLimit : 0,
        // Buildings
        farms : 0, lumberMills : 0, huntingCamps : 0, quarries : 0, huts : 0, smithies : 0,  schools : 0, storerooms : 0,
        // Building Prices
        farmPrice : 1, lumberMillPrice : 2, huntingCampPrice : 2, quarryPrice : 2, hutPrice : 2, smithyPrice : 3, schoolPrice : 3, storeroomPrice : 5,
        // Villager Assignments
        idlers : 0, farmers : 0, foresters : 0, hunters : 0, miners : 0, builders : 0, thinkers : 0,
        // Progress Unlocks
        unlockFoodSurplus : false, unlockHuts : false, unlockVillagers : false, unlockResearch : false,
        // Technologies
        farming : false, basicConstruction : false, minersUnlocked : false,
        // Technology Prices
        farmingPrice : 5, basicConstructionPrice : 5,
        // System
        fps : 50, msPerTick : 200, timeOfLastTick : Date.now(), timeOfLastVillagerCreation : Date.now(), creatingAVillager : false}
}

game.run = function gameLoop()
{
    fpsMeter.tickStart();
    if (Date.now() - game.timeOfLastTick >= game.msPerTick)
    {
        UnHideUIElements();
        updateResources();
        updateResourceLimits();
        determineVillagerCreation();
        updateDisplayValues();

        game.timeOfLastTick = Date.now();
    }
    saveState();
    fpsMeter.tick();
};

function determineVillagerCreation()
{
    if (game.villagerLimit > game.villagers && game.food > 0)
    {
        if (game.creatingAVillager === false)
        {
            game.creatingAVillager = true;
            game.timeOfLastVillagerCreation = Date.now();
        }

        var timeSinceLastVillagerInMS = Date.now() - game.timeOfLastVillagerCreation;
        var rand = Math.random() * 50000;
        var create = rand < timeSinceLastVillagerInMS;

        if (create)
        {
            // create villager
            var spacesAvailable = game.villagerLimit - game.villagers;

            var villagersToCreate = Math.floor(Math.sqrt(game.villagers)) - 3;
            if (villagersToCreate > (spacesAvailable))
                villagersToCreate = spacesAvailable;
            if (villagersToCreate < 1)
                villagersToCreate = 1;

            updateVillagerCount(villagersToCreate);
            assignIdler(villagersToCreate);

            game.creatingAVillager = false;
        }
    }
}

function updateResourceRateDisplay(rate, elementId)
{
    rate *= (1000 / game.msPerTick);
    rate = myRound(rate, 2);
    if (rate > 0) rate = '+' + rate;
    $('#' + elementId).text(rate);
}

function updateResources()
{
    // Food
    var foodBonusMultiplier = 1 + (0.05 * game.farms);
    var foodRate = (0.5 * game.farmers) * foodBonusMultiplier;  // add up basic elements
    foodRate = foodRate - .45 * game.villagers;                 // account for eating
    foodRate = 0.1 * foodRate * (game.msPerTick / 1000);        // apply arbitrary global multiplier and gameSpeed multiplier
    updateResource('food', foodRate);
    updateResourceRateDisplay(foodRate, 'foodRate');

    // Lumber
    var lumberBonusMultiplier = 1 + (0.1 * game.lumberMills);
    var lumberRate = (0.3 * game.foresters) * lumberBonusMultiplier;    // add up basic elements
    lumberRate = 0.1 * lumberRate * (game.msPerTick / 1000);            // apply arbitrary global multiplier and gameSpeed multiplier
    updateResource('lumber', lumberRate);
    updateResourceRateDisplay(lumberRate, 'lumberRate');

    // Stone
    var stoneBonusMultiplier = 1 + (0.06 * game.quarries);
    var stoneRate = (0.1 * game.miners) * stoneBonusMultiplier;     // add up basic elements
    stoneRate = 0.1 * stoneRate * (game.msPerTick / 1000);          // apply arbitrary global multiplier and gameSpeed multiplier
    updateResource('stone', stoneRate);
    updateResourceRateDisplay(stoneRate, 'stoneRate');

    // Research
    var researchBonusMultiplier = 1 + (0.06 * game.schools);
    var researchRate = (0.2 * game.thinkers) * researchBonusMultiplier; // add up basic elements
    researchRate = 0.1 * researchRate * (game.msPerTick / 1000);        // apply arbitrary global multiplier and gameSpeed multiplier
    updateResource('research', researchRate);
    updateResourceRateDisplay(researchRate, 'researchRate');

    // Starvation
    if (game.food < 0 && game.villagers > 0)
    {
        game.food = 0;
        updateVillagerCount(-1);
        killWorkersFromStarvation();
    }
}

function updateResourceLimits()
{
    game.foodLimit = (40 + (40 * game.storerooms)) * (1 + 0.05 * game.storerooms);
    game.foodLimit = myRound(game.foodLimit, 0);

    game.lumberLimit = (24 + (24 * game.storerooms)) * (1 + 0.05 * game.storerooms);
    game.lumberLimit = myRound(game.lumberLimit, 0);

    game.leatherLimit = (20 + (20 * game.storerooms)) * (1 + 0.05 * game.storerooms);
    game.leatherLimit = myRound(game.leatherLimit, 0);

    game.stoneLimit = (10 + (10 * game.storerooms)) * (1 + 0.05 * game.storerooms);
    game.stoneLimit = myRound(game.stoneLimit, 0);

    game.villagerLimit = 2 * game.huts;
}

//food
//cotton
//wood
//leather
//stone
//bronze
//iron
//steel


function killWorkersFromStarvation()
{
    if (game.idlers > 0)
    {
        assignIdler(-1);
        return;
    }
    if (game.foresters > 0)
    {
        killWorker('foresters');
        return;
    }
    if (game.thinkers > 0)
    {
        killWorker('thinkers');
        return;
    }
    if (game.farmers > 0)
    {
        killWorker('farmers');
        return;
    }
}

// Preconditions: name must be id of html element and name of member of game object...
function updateResource(name, amount)
{
    var newAmount = game[name] + amount;
    if (newAmount > game[name + 'Limit'])
        game[name] = game[name + 'Limit']; // set it to the limit
    else
        game[name] = newAmount;
}

// Research
function makeDiscovery(technologyName)
{
    var canAfford = game['research'] >= game[technologyName + 'Price'];
    if (canAfford)
    {
        updateResource('research', -game[technologyName + 'Price']);
        game[technologyName] = true;
    }
}