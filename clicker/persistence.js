/**
 * Created with IntelliJ IDEA.
 * User: ehicks
 * Date: 5/12/14
 * Time: 4:05 PM
 * To change this template use File | Settings | File Templates.
 */
    // Local Storage
function saveState()
{
    if(typeof(Storage)!=="undefined")
    {
        // Code for localStorage/sessionStorage.
        localStorage['gameInProgress'] = 'true';
        for (var gameVar in game)
        {
            if (game.hasOwnProperty(gameVar))
            {
                if (gameVar.length > 3 && gameVar.substring(0, 4) === 'time')
                    continue;
                if (gameVar.length > 2 && gameVar.substring(0, 3) === 'run')
                    continue;
                localStorage[gameVar] = game[gameVar];
            }
        }
    }
    else
    {
        // Sorry! No Web Storage support..
    }
}

function restoreState()
{
    if(typeof(Storage)!=="undefined")
    {
        // Code for localStorage/sessionStorage.
        for (var storedVar in localStorage)
        {
            if (localStorage.hasOwnProperty(storedVar))
            {
                if (storedVar.length > 0 && storedVar.substring(0, 1) === '_')
                    continue;
                if (storedVar.length > 2 && storedVar.substring(0, 4) === 'run')
                    continue;

                if (localStorage[storedVar] === 'true')
                    game[storedVar] = true;
                if (localStorage[storedVar] === 'false')
                    game[storedVar] = false;

                if (localStorage[storedVar] !== 'true' && localStorage[storedVar] !== 'false')
                    game[storedVar] = myRound(parseFloat(localStorage[storedVar]), 2);
            }
        }
    }
    else
    {
        // Sorry! No Web Storage support..
    }
}

function clearState()
{
    if(typeof(Storage)!=="undefined")
    {
        if (confirm('Are you sure?'))
        {
            localStorage.clear();
            game = setGameDefaults();
            updateDisplayValues();
            reHideUIElementsOnClear();
        }
    }
    else
    {
        // Sorry! No Web Storage support..
    }
}