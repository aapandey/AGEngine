package AGEngine.time;

import java.io.Serializable;

import AGEngine.core.Engine;
import AGEngine.objects.ITickable;
import AGEngine.time.TimeEnums.TimeIterationType;

/**
 * 
 * @author Abhishek-PC
 * http://tutorials.jenkov.com/java-date-time/system-currenttimemillis.html
 * https://stackoverflow.com/questions/4863658/how-to-get-system-time-in-java-without-creating-a-new-date
 * https://beginnersbook.com/2013/05/current-date-time-in-java/
 * https://alvinalexander.com/java/edu/pj/pj010018
 */
public class Time implements Serializable, ITickable {

	private static final long serialVersionUID = 11110000L;
	
	private long _anchorTime;
	private long _tickSize;
	private long _time;

	private TimeIterationType _timeIterationType;
	private transient Engine _engine;

	@Override
	public void update(long tick) {
		// TODO Auto-generated method stub
		if(_timeIterationType == TimeIterationType.Real) {
			_time = System.currentTimeMillis() - _anchorTime;
			_time /= _tickSize;
		}
		else {
			_time = _engine.frameCount - _anchorTime;
			_time /= _tickSize;
		}
	}
	
	public long get_time() {
		return _time;
	}
	
	public Time(Time anchor, long tickSize, TimeIterationType timeIterationType, Engine engine) 
	{
		_anchorTime = (anchor == null) ? 0 : anchor.get_time();
		_time = _anchorTime;
		_tickSize = tickSize;
		_timeIterationType = timeIterationType;
		_engine = engine;
		this.update(1L);
	}
}
