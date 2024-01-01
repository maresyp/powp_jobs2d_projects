package edu.kis.powp.jobs2d.gui;

import edu.kis.legacy.drawer.panel.DrawPanelController;
import edu.kis.legacy.drawer.shape.LineFactory;
import edu.kis.powp.appbase.Application;
import edu.kis.powp.jobs2d.Job2dDriver;
import edu.kis.powp.jobs2d.drivers.adapter.LineDriverAdapter;
import edu.kis.powp.jobs2d.events.SelectMacro2OptionListener;
import edu.kis.powp.jobs2d.events.SelectRunCurrentCommandOptionListener;
import edu.kis.powp.jobs2d.features.DrawerFeature;
import edu.kis.powp.jobs2d.features.DriverFeature;
import edu.kis.powp.jobs2d.features.driverTransofrmation.TransformingDriver;
import edu.kis.powp.jobs2d.features.driverTransofrmation.modifiers.RotationModifier;
import edu.kis.powp.jobs2d.features.driverTransofrmation.modifiers.ScalingModifier;
import edu.kis.powp.jobs2d.features.driverTransofrmation.modifiers.ScalingModifierFactory;
import edu.kis.powp.jobs2d.features.driverTransofrmation.modifiers.ShiftAxesModifier;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Extensions
{
    private static Job2dDriver currentDriver;
    private static Job2dDriver lineDriver;
    private static TransformingDriver scaledAndRotatedDriver;
    private static LineDriverAdapter specialLineSimulator;
    private static TransformingDriver flippedAndShiftedDriver;

    static
    {
        DrawPanelController drawerController = DrawerFeature.getDrawerController();

        // Driver for Line Simulator
        lineDriver = new LineDriverAdapter(drawerController, LineFactory.getBasicLine(), "basic");

        // Transformed Driver for Line Simulator - scaled to 25% and rotated by 70deg
        scaledAndRotatedDriver = new TransformingDriver(lineDriver);
        scaledAndRotatedDriver.addModifier(new ScalingModifier(0.25, 0.25));
        scaledAndRotatedDriver.addModifier(new RotationModifier(70));

        // Driver for Special Line Simulator
        specialLineSimulator = new LineDriverAdapter(drawerController, LineFactory.getSpecialLine(), "special");

        // Transformed Driver for Special Line Simulator - flipped in both planes and shifted axes: X(50) Y(-50)
        flippedAndShiftedDriver = new TransformingDriver(specialLineSimulator);
        flippedAndShiftedDriver.addModifier(ScalingModifierFactory.createHorizontalFlipModifier());
        flippedAndShiftedDriver.addModifier(ScalingModifierFactory.createVerticalFlipModifier());
        flippedAndShiftedDriver.addModifier(new ShiftAxesModifier(50, -50));
    }

    public static void setupExtensions(Application application)
    {
        application.addComponentMenu(Extensions.class, "Extensions");
        application.addComponentMenuElement(Extensions.class, "Run command", new SelectRunCurrentCommandOptionListener(DriverFeature.getDriverManager()));
        application.addComponentMenuElement(Extensions.class, "Load macro", new SelectMacro2OptionListener());

        application.addComponentMenuElementWithCheckBox(
                Extensions.class,
                "Line Simulator",
                new MyListener(lineDriver),
                false
        );

        application.addComponentMenuElementWithCheckBox(
                Extensions.class,
                "Line Simulator  - scale: 1/4, rotation: 70deg",
                new MyListener(scaledAndRotatedDriver),
                false
        );

        application.addComponentMenuElementWithCheckBox(
                Extensions.class,
                "Special line Simulator",
                new MyListener(specialLineSimulator),
                false
        );

        application.addComponentMenuElementWithCheckBox(
                Extensions.class,
                "Special Line Simulator  - flipped X and Y, shifted X and Y",
                new MyListener(flippedAndShiftedDriver),
                false
        );
    }

    private static class MyListener implements ActionListener
    {
        private Job2dDriver myDriver;

        public MyListener(Job2dDriver myDriver)
        {
            this.myDriver = myDriver;
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            if ((Extensions.currentDriver != null) && (Extensions.currentDriver.getClass() == myDriver.getClass()))
            {
                DriverFeature.getDriverManager().setCurrentDriver(null);
                System.out.println("wylanczam");
                currentDriver = null;
            }
            else
            {
                DriverFeature.getDriverManager().setCurrentDriver(myDriver);
                System.out.println("wlanczam");
                currentDriver = myDriver;
            }
        }
    }
}