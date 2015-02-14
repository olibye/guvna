Version 2.0
Entry actions leaving actions are now registered on the state machine.
This allows, for example, the AI for NPC game characters to share the same state machine, but have different actions. It also means that passing the FSM to the action is not so necessary.