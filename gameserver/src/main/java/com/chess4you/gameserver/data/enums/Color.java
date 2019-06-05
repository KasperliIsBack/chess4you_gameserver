package com.chess4you.gameserver.data.enums;

public enum Color {
    Black, White;
    public static Color getColorById(int id) {
        switch (id) {
            case 0:
                return Black;
            case 1:
                return White;
                default:
                    return Black;
        }
    }

    public static Color getOtherColor(Color color) {
        switch (color){
            case Black:
                return White;
            case White:
                return Black;
            default:
                return White;
        }
    }
}
