package com.votingsystem.to.converters;

import com.votingsystem.entity.Menu;
import com.votingsystem.to.MenuTo;

/**
 * @author Paz1506
 * Converter MenuTo - Menu.
 */

public class MenuConverter {

    //Get MenuTo from Menu
    public static MenuTo getToFromMenu(Menu menu) {
        return new MenuTo(menu.getId(), menu.getName(), menu.getDate());
    }

    //GetDish from TO (without Rest & Dishes (-> set to сontroller))
    public static Menu getMenuFromTo(MenuTo newMenu) {
        Menu menu = new Menu();
        menu.setId(newMenu.getId());
        menu.setDate(newMenu.getDate());
        menu.setName(newMenu.getName());
        return menu;
    }
}
