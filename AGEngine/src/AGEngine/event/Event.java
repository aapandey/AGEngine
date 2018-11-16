package AGEngine.event;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

public class Event implements Serializable{
	
	private String _eventType;
	private List<Object> _eventPar;
	private String _timeName;
	private float _time;
	private boolean _backup;
	private float _frame;
	private int _connectionID;

	public Event(String eventType, List<Object> eventPar, String timeName, float time, boolean backup, int connectionID) 
	{
		_eventType = eventType;
		_eventPar = eventPar;
		_timeName = timeName;
		_time = time;
		_backup = backup;
		_connectionID = connectionID;
	}
	
	public List<Object> get_eventPar() {
		return _eventPar;
	}

	public boolean is_backup() {
		return _backup;
	}

	public String get_eventType() {
		return _eventType;
	}

	public String get_timeName() {
		return _timeName;
	}

	public float get_time() {
		return _time;
	}

	public float get_frame() {
		return _frame;
	}

	public int get_connectionID() {
		return _connectionID;
	}

	public void set_eventPar(List<Object> _eventPar) {
		this._eventPar = _eventPar;
	}
	
	public void set_backup(boolean _backup) {
		this._backup = _backup;
	}
	
	public void set_connectionID(int _connectionID) {
		this._connectionID = _connectionID;
	}

}

class ScoredEvent {
	public Event event;
	public float score;

	public ScoredEvent(Event event, float score) {
		this.event = event;
		this.score = score;
	}
}

class CompareEvent implements Comparator<ScoredEvent> {
	public int compare(ScoredEvent event1, ScoredEvent event2) {
		if (event1.score < event2.score) {
			return -1;
		} else if (event1.score > event2.score) {
			return 1;
		} else {
			return 0;
		}
	}
}
