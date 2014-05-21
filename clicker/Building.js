/**
 * Created with IntelliJ IDEA.
 * User: ehicks
 * Date: 5/15/14
 * Time: 3:12 PM
 * To change this template use File | Settings | File Templates.
 */

// building should be a generic object, we will then instantiate a building for huts, a building for farms, etc...
// all farms will be represented by a single 'farms' object, that object will track how many farms exist...

//function Building(name, id, rowId, price, priceType, priceMultiplier, priceId, resourceRate)
//{
//    var myBuilding = {};
//    // IMMUTABLE
//    this.name = name;
//    this.id = id;
//    this.rowId = rowId;
//    this.priceId = priceId;
//    this.priceType = priceType;
//    this.priceMultiplier = priceMultiplier;
//    this.resourceRate = resourceRate;
//
//    // MUTABLE
//    this.count = 0;
//    this.price = price;
//}
//
//Building.prototype.build = function ()
//{
//    var canAfford = game[this.priceType] >= game[this.price];
//    if (canAfford)
//    {
//        updateResource(this.priceType, -game[this.price]);
//
//        this.count += 1;
//        $('#' + this.id).text(this.count.toString());
//
//        if (this.resourceRate !== null)
//        {
//            this.resourceRate += 1;
//            $('#' + this.resourceRate).text(this.resourceRate.toString());
//        }
//
//        game[this.price] = myRound(game[this.price] * this.priceMultiplier, 2);
//        $('#' + this.priceId).text(game[this.price].toString());
//    }
//};