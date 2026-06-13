package be.uantwerpen.fti.ei.Game.Components;

/** Marker interface for ECS-style components. */
public interface Component {
}
//Entity
//= object in the game world, like Enemy or Tower
//
//Component
//= pure data, like position, health, velocity
//
//System
//= logic that processes components/entities, like MovementSystem
// example of my ECS: GameEntity owns PositionComponent.
//MovementSystem processes enemies and updates their positions.