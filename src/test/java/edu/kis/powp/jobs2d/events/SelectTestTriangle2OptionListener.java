package edu.kis.powp.jobs2d.events;

import edu.kis.powp.jobs2d.command.ComplexCommandFactory;
import edu.kis.powp.jobs2d.command.DriverCommand;
import edu.kis.powp.jobs2d.command.manager.CommandManager;
import edu.kis.powp.jobs2d.features.CommandsFeature;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class SelectTestTriangle2OptionListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        CommandManager manager = CommandsFeature.getDriverCommandManager();
        manager.setCurrentCommand(ComplexCommandFactory.getTriangle());
    }
}
