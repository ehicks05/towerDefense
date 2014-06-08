// Villager
function updateVillagerCount(amount)
{
    game.villagers += amount;
}

// Worker
function assignIdler(amount)
{
    game.idlers += amount;
}

function assignWorker(job)
{
    if (game.idlers > 0)
    {
        game[job] += 1;
        game.idlers -= 1;
    }
}
function unAssignWorker(job)
{
    if (game[job] > 0)
    {
        game[job] -= 1;
        game.idlers += 1;
    }
}

function killWorker(job)
{
    if (game[job] > 0)
        game[job] -= 1;
}