package org.chwin.firefighting.apiserver.net;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Writer;

public class PrintWriterExt extends PrintWriter {

	public PrintWriterExt(File file) throws FileNotFoundException {
		super(file);
		// TODO Auto-generated constructor stub
	}

	public PrintWriterExt(String string) throws FileNotFoundException {
		super(new File(string)) ;     
	}

	public PrintWriterExt() throws FileNotFoundException {
		super(new PrintWriterExt());
	}
	
	public String line;
	public void println(String s)
	{
		this.line=s;
	}
	
	

}
