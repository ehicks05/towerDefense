function buildBuilding(priceType, priceAmount, buildingName)
{
    var priceIncreaseMultiplier = 1.07;
    if (buildingName === 'huts') priceIncreaseMultiplier = 1.05;

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
function buildQuarry()
{
    buildBuilding('lumber', 'quarryPrice', 'quarries');
    game.minersUnlocked = true;
}