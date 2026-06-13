package be.uantwerpen.fti.ei.Game.Level;

public enum TileType {
    PATH, //enemies follow this
    BUILDABLE, //you can place towers here and they become occupied
    BLOCKED, //obstruction
    SCENERY,// //all other tiles
    BASE, //tile wich enemies go to/attack
    SPAWN, //where enemies begin
    OCCUPIED //tower is placed here (was buildable)
}
