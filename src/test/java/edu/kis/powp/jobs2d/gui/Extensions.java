package edu.kis.powp.jobs2d.gui;

import edu.kis.legacy.drawer.panel.DrawPanelController;
import edu.kis.legacy.drawer.shape.LineFactory;
import edu.kis.powp.appbase.Application;
import edu.kis.powp.jobs2d.Job2dDriver;
import edu.kis.powp.jobs2d.drivers.DriverMacro;
import edu.kis.powp.jobs2d.drivers.adapter.LineDriverAdapter;
import edu.kis.powp.jobs2d.events.SelectMacro2OptionListener;
import edu.kis.powp.jobs2d.events.SelectMacroStartListener;
import edu.kis.powp.jobs2d.events.SelectMacroStopListener;
import edu.kis.powp.jobs2d.events.SelectRunCurrentCommandOptionListener;
import edu.kis.powp.jobs2d.features.DrawerFeature;
import edu.kis.powp.jobs2d.features.DriverFeature;
import edu.kis.powp.jobs2d.features.driverTransofrmation.TransformationModifier;
import edu.kis.powp.jobs2d.features.driverTransofrmation.TransformingDriver;
import edu.kis.powp.jobs2d.features.driverTransofrmation.modifiers.RotationModifier;
import edu.kis.powp.jobs2d.features.driverTransofrmation.modifiers.ScalingModifier;
import edu.kis.powp.jobs2d.features.driverTransofrmation.modifiers.ScalingModifierFactory;
import edu.kis.powp.jobs2d.features.driverTransofrmation.modifiers.ShiftAxesModifier;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Extensions
{
    private static TransformingDriver lineTransformingDriver;
    private static TransformingDriver specialLineTransformingDriver;

    static
    {
        DrawPanelController drawerController = DrawerFeature.getDrawerController();

        // Driver for Line Simulator
        Job2dDriver lineDriver = new LineDriverAdapter(drawerController, LineFactory.getBasicLine(), "basic");

        // Driver for Special Line Simulator
        LineDriverAdapter specialLineSimulator = new LineDriverAdapter(drawerController, LineFactory.getSpecialLine(), "special");

        // Transforming Driver for Line Simulator
        lineTransformingDriver = new TransformingDriver(lineDriver);

        // Transforming Driver for Special Line Simulator
        specialLineTransformingDriver = new TransformingDriver(specialLineSimulator);
    }

    public static void setupExtensions(Application app)
    {
        // Add a new menu
        app.addComponentMenu(Extensions.class, "Extensions");

        // Add various extensions
        app.addComponentMenuElement(Extensions.class, "Run command", new SelectRunCurrentCommandOptionListener(DriverFeature.getDriverManager()));
        app.addComponentMenuElement(Extensions.class, "Load macro", new SelectMacro2OptionListener());

        // Add modifiers
        addMenuElementWithCheckbox(app, "Scale: 1/4", new ModifierListener(new ScalingModifier(0.25, 0.25)));
        addMenuElementWithCheckbox(app, "Rotation: 70deg", new ModifierListener(new RotationModifier(70)));
        addMenuElementWithCheckbox(app, "Horizontal flip", new ModifierListener(ScalingModifierFactory.createHorizontalFlipModifier()));
        addMenuElementWithCheckbox(app, "Vertical flip", new ModifierListener(ScalingModifierFactory.createVerticalFlipModifier()));
        addMenuElementWithCheckbox(app, "Shifted X and Y", new ModifierListener(new ShiftAxesModifier(50, -50)));
        addMenuElementWithCheckbox(app, "Macro", new MacroListener());
    }

    private static void addMenuElementWithCheckbox(Application app, String label, ActionListener listener)
    {
        app.addComponentMenuElementWithCheckBox(Extensions.class, label, listener, false);
    }

    private static class ModifierListener implements ActionListener
    {
        private boolean enabled;
        private TransformationModifier modifier;

        public ModifierListener(TransformationModifier modifier)
        {
            enabled = false;
            this.modifier = modifier;
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            enabled = !enabled;

            if (enabled)
            {
                lineTransformingDriver.addModifier(modifier);
                specialLineTransformingDriver.addModifier(modifier);
            }
            else
            {
                lineTransformingDriver.removeModifier(modifier);
                specialLineTransformingDriver.removeModifier(modifier);
            }
        }
    }

    private static class MacroListener implements ActionListener
    {
        private boolean enabled;
        private DriverMacro driverMacro;
        private ActionListener startMacro, stopMacro;

        public MacroListener()
        {
            enabled = false;
            driverMacro = new DriverMacro();
            startMacro = new SelectMacroStartListener(driverMacro, DriverFeature.getDriverManager());
            stopMacro = new SelectMacroStopListener(driverMacro, DriverFeature.getDriverManager());
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            enabled = !enabled;

            if (enabled)
            {
                startMacro.actionPerformed(e);
            }
            else
            {
                stopMacro.actionPerformed(e);
            }
        }
    }

    public static TransformingDriver getLineSimulator()
    {
        return lineTransformingDriver;
    }

    public static TransformingDriver getSpecialLineSimulator()
    {
        return specialLineTransformingDriver;
    }
}
