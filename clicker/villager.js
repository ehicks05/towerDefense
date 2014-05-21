// Villager
function addVillager(amount)
{
    game.villagers += amount;
    $('#villagers').text(game.villagers.toString());
}

// Worker
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