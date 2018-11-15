package AGEngine.event;

/**
 * 
 * @author Abhishek-PC
 * https://www.javatpoint.com/event-handling-in-java
 * https://www.tutorialspoint.com/awt/awt_event_handling.htm
 * https://docs.oracle.com/javase/tutorial/uiswing/events/index.html
 * https://www.javaworld.com/article/2077218/core-java/java-and-event-handling.html
 */
public class EventEnums {
	
	// Event type used by engine
	public enum EngineEventType 
	{
		KeyoardInput, StartRecording, StopRecording, PlayRecording, Interrupt
	}

	// Event Type used in game
	public enum EventType 
	{
		PlayerDies, PlayerCollision, PlayerSpawn, NULL
	}
}
