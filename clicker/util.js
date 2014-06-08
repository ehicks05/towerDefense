function enableDebugging(){$('*').removeClass('hidden');}

function myRound(value, places)
{
    var multiplier = Math.pow(10, places);
    return (Math.round(value * multiplier) / multiplier);
}

// -------- DISPLAY UTILS --------

function updateDisplayValues()
{
    for (var gameVar in game)
    {
        if (game.hasOwnProperty(gameVar))
        {
            if ($('#' + gameVar).length)  // does the element exist?
                $('#' + gameVar).text(myRound(game[gameVar], 2));
        }
    }
}

function selectTab(id)
{
    if (!$('#' + id).hasClass('selected'))
    {
        if (id == 'buildingsTabButton')
        {
            $('#villagersTable').addClass('hidden');
            $('#researchTable').addClass('hidden');
            $('#buildingsTable').removeClass('hidden');
        }
        if (id == 'villagersTabButton')
        {
            $('#buildingsTable').addClass('hidden');
            $('#researchTable').addClass('hidden');
            $('#villagersTable').removeClass('hidden');
        }
        if (id == 'researchTabButton')
        {
            $('#buildingsTable').addClass('hidden');
            $('#villagersTable').addClass('hidden');
            $('#researchTable').removeClass('hidden');
        }
    }

    $(".navTab").removeClass('selected');
    $('#' + id).addClass('selected');
}