function unHide(id) {$('#' + id).removeClass('hidden');}
function reHide(id) {$('#' + id).addClass('hidden');}

function disable(id) {$('#' + id).prop('disabled', true);}
function enable(id) {$('#' + id).prop('disabled', false);}

function reHideUIElementsOnClear()
{
    reHide('buildingsTabButton');
    reHide('buildingsTable');
    reHide('hutsRow');

    reHide('villagersTabButton');
    reHide('villagersRow');
    reHide('idlersRow');
    reHide('farmersRow');
    reHide('forestersRow');

    reHide('researchTabButton');
    reHide('researchTable');
    reHide('researchRow');
    reHide('thinkersRow');
    reHide('farmingRow');
    reHide('basicConstructionRow');

    reHide('farmsRow');
    enable('farmingButton');

    reHide('lumberMillsRow');
    reHide('storeroomsRow');
    reHide('stoneRow');
    reHide('quarriesRow');
    reHide('minersRow');
    enable('basicConstructionButton');
}

function UnHideUIElementsOnStartup()
{
    if (game.unlockBuildings === true)
    {
        $('#buildingsTabButton').addClass('selected');

        unHide('buildingsTabButton');
        unHide('buildingsTable');
        unHide('hutsRow');
    }
    if (game.unlockVillagers === true)
    {
        unHide('villagersTabButton');
        unHide('villagersRow');
        unHide('idlersRow');
        unHide('farmersRow');
        unHide('forestersRow');
    }
    if (game.unlockResearch === true)
    {
        unHide('researchTabButton');
        unHide('researchRow');
        unHide('thinkersRow');
        unHide('farmingRow');
        unHide('basicConstructionRow');
    }
    if (game.farming === true)
    {
        unHide('farmsRow');
        disable('farmingButton');
    }
    if (game.basicConstruction === true)
    {
        unHide('lumberMillsRow');
        unHide('storeroomsRow');
        unHide('stoneRow');
        unHide('quarriesRow');
        unHide('minersRow');
        disable('basicConstructionButton');
    }
}

function UnHideUIElements()
{
    if (game.unlockBuildings === false && game.food > 2.2)
    {
        game.unlockBuildings = true;
        $('#buildingsTabButton').addClass('selected');

        unHide('buildingsTabButton');
        unHide('buildingsTable');
        unHide('hutsRow');
    }
    if (game.unlockVillagers === false && game.villagers > 0)
    {
        game.unlockVillagers = true;
        unHide('villagersTabButton');
        unHide('villagersRow');
        unHide('idlersRow');
        unHide('farmersRow');
        unHide('forestersRow');
    }
    if (game.unlockResearch === false && game.villagers > 4)
    {
        game.unlockResearch = true;
        unHide('researchTabButton');
        unHide('researchRow');
        unHide('thinkersRow');

        unHide('farmingRow');
        unHide('basicConstructionRow');
    }
    if (game.farming === true && $('#farmsRow').hasClass('hidden'))
    {
        unHide('farmsRow');
        disable('farmingButton');
    }
    if (game.basicConstruction === true && $('#lumberMillsRow').hasClass('hidden'))
    {
        unHide('lumberMillsRow');
        unHide('storeroomsRow');
        unHide('stoneRow');
        unHide('quarriesRow');
        disable('basicConstructionButton');
        unHide('minersRow');
    }
}