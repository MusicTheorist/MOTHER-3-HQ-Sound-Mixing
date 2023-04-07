package org.musictheorist.mother3hqaudiopatcher.ui;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.Priority;

public record SpacedNode (Node node, Insets margin, Priority grow) {}