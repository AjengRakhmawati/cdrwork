package com.solusi247.klas.jawa;

public class SubscriberPrefix {
	String _prefix_id;
	String _region_id;
	String _city;
	
	public SubscriberPrefix(String _prefix_id,String _region_id, String _city) {
		this._prefix_id = _prefix_id;
		this._region_id = _region_id;
		this._city = _city;
	}
	
	
	public String get_prefix_id() {
		return _prefix_id;
	}
	public void set_prefix_id(String _prefix_id) {
		this._prefix_id = _prefix_id;
	}
	public String get_region_id() {
		return _region_id;
	}
	public void set_region_id(String _region_id) {
		this._region_id = _region_id;
	}
	public String get_city() {
		return _city;
	}
	public void set_city(String _city) {
		this._city = _city;
	}
}
