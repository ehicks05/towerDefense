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
        // Resource Rates
        foodRate : 0, lumberRate : 0, leatherRate : 0, stoneRate : 0, researchRate : 0,
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
        farming : false, basicConstruction : false,
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
            addVillager(1);
            assignIdler(true);

            game.creatingAVillager = false;
        }
    }
}

function updateResources()
{
    // Food
    var foodBonusMultiplier = 1 + (0.1 * game.farms);
    var foodRate = (0.5 * game.farmers) * foodBonusMultiplier;  // add up basic elements
    foodRate = foodRate - .45 * game.villagers;                 // account for eating
    foodRate = 0.1 * foodRate * (game.msPerTick / 1000);        // apply arbitrary global multiplier and gameSpeed multiplier
    foodRate = myRound(foodRate, 3);                            // round it

    updateResource('food', foodRate);
    $('#foodRate').text(foodRate);

    // Lumber
    var lumberBonusMultiplier = 1 + (0.2 * game.lumberMills);
    var lumberRate = (0.3 * game.foresters) * lumberBonusMultiplier;    // add up basic elements
    lumberRate = 0.1 * lumberRate * (game.msPerTick / 1000);            // apply arbitrary global multiplier and gameSpeed multiplier
    lumberRate = myRound(lumberRate, 3);                                // round it

    updateResource('lumber', lumberRate);
    $('#lumberRate').text(lumberRate);

    // Research
    var researchBonusMultiplier = 1 + (0.06 * game.schools);
    var researchRate = (0.2 * game.thinkers) * researchBonusMultiplier; // add up basic elements
    researchRate = 0.1 * researchRate * (game.msPerTick / 1000);        // apply arbitrary global multiplier and gameSpeed multiplier
    researchRate = myRound(researchRate, 3);                            // round it

    updateResource('research', researchRate);
    $('#researchRate').text(researchRate);

    // Starvation
    if (game.food < 0 && game.villagers > 0)
    {
        game.food = 0;
        addVillager(-1);
        killWorkersFromStarvation();
    }
}

function updateResourceLimits()
{
    game.foodLimit = 20 + (20 * game.storerooms);
    $('#foodLimit').text(game.foodLimit);
    game.lumberLimit = 10 + (20 * game.storerooms);
    $('#lumberLimit').text(game.lumberLimit);
    game.leatherLimit = 10 + (20 * game.storerooms);
    $('#leatherLimit').text(game.leatherLimit);
    game.stoneLimit = 5 + (20 * game.storerooms);
    $('#stoneLimit').text(game.stoneLimit);
    game.villagerLimit = 2 * game.huts;
    $('#villagerLimit').text(game.villagerLimit);
}

function killWorkersFromStarvation()
{
    if (game.idlers > 0)
    {
        assignIdler(false);
        return;
    }
    if (game.farmers > 0)
    {
        killWorker('farmers');
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
}

// Preconditions: name must be id of html element and name of member of game object...
function updateResource(name, amount)
{
    var newAmount = game[name] + amount;
    if (newAmount > game[name + 'Limit'])
    {
        game[name] = game[name + 'Limit']; // set it to the limit
    }
    else
        game[name] += amount;

    $('#' + name).text(myRound(game[name], 2).toString());
}

function addVillager(amount)
{
    game.villagers += amount;
    $('#villagers').text(game.villagers.toString());
}

// Villager setters
function assignIdler(status)
{
    if (status)
        game.idlers += 1;
    if (!status)
        game.idlers -= 1;
    $('#idlers').text(game.idlers.toString());
}

function assignWorker(job)
{
    if (game.idlers > 0)
    {
        game[job] += 1;
        game.idlers -= 1;
    }

    $('#' + job).text(game[job].toString());
    $('#idlers').text(game.idlers.toString());
}
function unAssignWorker(job)
{
    if (game[job] > 0)
    {
        game[job] -= 1;
        game.idlers += 1;
    }
    $('#' + job).text(game[job].toString());
    $('#idlers').text(game.idlers.toString());
}
function killWorker(job)
{
    if (game[job] > 0)
        game[job] -= 1;

    $('#' + job).text(game[job].toString());
}

// building setters
function buildBuilding(priceType, priceAmount, buildingName)
{
    var priceIncreaseMultiplier = 1.15;
    if (buildingName === 'huts') priceIncreaseMultiplier = 1.3;

    var canAfford = game[priceType] >= game[priceAmount];
    if (canAfford)
    {
        updateResource(priceType, -game[priceAmount]);
        game[priceAmount] = myRound(game[priceAmount] * priceIncreaseMultiplier, 2);
        $('#' + priceAmount).text(game[priceAmount].toString());

        game[buildingName] += 1;
        $('#' + buildingName).text(game[buildingName].toString());
    }
}

function buildHut(){buildBuilding('food', 'hutPrice', 'huts');}
function buildFarm(){buildBuilding('lumber', 'farmPrice', 'farms');}
function buildLumberMill(){buildBuilding('lumber', 'lumberMillPrice', 'lumberMills');}
function buildStoreroom(){buildBuilding('lumber', 'storeroomPrice', 'storerooms');}

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