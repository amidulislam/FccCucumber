package com.tavant;

import java.util.ArrayList;

public class Step {
	public class Codebeautify {
		 Steps StepsObject;


		 // Getter Methods 

		 public Steps getSteps() {
		  return StepsObject;
		 }

		 // Setter Methods 

		 public void setSteps(Steps stepsObject) {
		  this.StepsObject = stepsObject;
		 }
		}
		public class Steps {
		 ArrayList < Object > step = new ArrayList < Object > ();
		 private String _id;
		 private String _last;


		 // Getter Methods 

		 public String get_id() {
		  return _id;
		 }

		 public String get_last() {
		  return _last;
		 }

		 // Setter Methods 

		 public void set_id(String _id) {
		  this._id = _id;
		 }

		 public void set_last(String _last) {
		  this._last = _last;
		 }
		}


	
}
