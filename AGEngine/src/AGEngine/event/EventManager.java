package AGEngine.event;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import AGEngine.core.Engine;
import AGEngine.event.EventEnums.EventPriorities;
import AGEngine.time.Time;

public class EventManager {
	
	protected Map<String, EventPriorities> eventPriorities;
	protected Map<String, Time> timeNames;
	protected Engine engine;
	protected Map<String, Map<Integer, PriorityQueue<ScoredEvent>>> timeQueues;
	protected Map<String, Map<Integer, PriorityQueue<ScoredEvent>>> timeQueuesBackup;
	protected Map<String, Map<Integer, PriorityQueue<ScoredEvent>>> recordedQueues;

	public Map<Integer, Queue<Event>> fromServerWriteQueues;
	public Queue<Event> fromClientWriteQueue;
	Queue<Event> startupEvents;

	public Time replayTimeInFrames;
	public float replayStartFrame;

	public boolean recording = false;
	public boolean playingRecording = false;

	public void setRecording(boolean recording) {
		this.recording = recording;
	}
	
	public EventManager(Engine engine) 
	{
		fromServerWriteQueues = new HashMap<>();
		fromClientWriteQueue = new LinkedList<>();
		startupEvents = new LinkedList<>();
		eventPriorities = new HashMap<>();
		timeNames = new HashMap<>();
		timeQueues = new HashMap<>();
		timeQueuesBackup = new HashMap<>();
		recordedQueues = new HashMap<>();
		this.engine = engine;
	}

	public void registerEventType(String eventType, EventPriorities defaultEventPriority) 
	{
		eventPriorities.put(eventType, defaultEventPriority);
	}

	public void registerTimeline(Time timeline, String timelineName)
	{
		timeNames.put(timelineName, timeline);
	}

	public void raiseEvent(Event event, boolean toBroadcast) 
	{
		if (playingRecording)
			return;
	}

	public void putInEventQueues(Event event, float score, Map<String, Map<Integer, PriorityQueue<ScoredEvent>>> queues) 
	{

	}

	public void handleEvents() 
	{

	}

	public boolean isEventQueuesEmpty(Map<Integer, PriorityQueue<ScoredEvent>> eventQueues) 
	{
		return false;
	}

	public void finishPlayingRecordedEvents() 
	{
		
	}

	public void playRecordedEvents(float frameTicSize) 
	{
		
	}

	private void recordEvent(ScoredEvent orderedEvent) 
	{
		
	}

	public void broadcastEvent(Event event, int connectionID) 
	{

	}
}
