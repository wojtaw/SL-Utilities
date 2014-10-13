package slideslive.output;

public class OutputWriter implements Output{
	
	public OutputWriter(){
		
	}
	
	public void printLog(String msg){
		System.err.println("INFO "+msg);
	}
	
	public boolean printErr(String msg){
		System.err.println("SERIOUS ERROR: "+msg);
		return false;
	}

}
