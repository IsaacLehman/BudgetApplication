package com.isaacsmobiledevelopment.lehmanit17.budgetapplication;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;

public class FileTools extends AppCompatActivity {
	
	File file;
	final String NEWLINE = "\n";
	String fileName;
	private FileInputStream fileInputStream;
	private Scanner scn;
	private Context context;


	public FileTools(String fileName, Context context) {
		this.context = context;
		this.fileName = fileName;
		FileSetup();
	}

	public void FileSetup() {
		if (FileExists(fileName)) {
			this.file = context.getFileStreamPath(fileName);
			Log.e("MainActivity","file found: " + fileName);
		} else {
			try {
				Log.e("MainActivity","file not found");
				// get input stream of file in assets:
				InputStream inputStream = context.getAssets().open(fileName);

				// Get OutputStream of file in internal storage:
				File f = context.getFileStreamPath(fileName);
				OutputStream outputStream = new FileOutputStream(f);

				// Copy data from input stream to output stream:
				ByteBuffer buffer = ByteBuffer.allocateDirect(1024 * 8);

				ReadableByteChannel ich = Channels.newChannel(inputStream);
				WritableByteChannel och = Channels.newChannel(outputStream);

				while (ich.read(buffer) > -1 || buffer.position() > 0)
				{
					buffer.flip();
					och.write(buffer);
					buffer.compact();
				}
				ich.close();
				och.close();

				this.file = context.getFileStreamPath(fileName);
			} catch (IOException e) {
				Toast.makeText(getBaseContext(), e.getMessage(),
						Toast.LENGTH_LONG).show();
				Log.d("MainActivity","file not made + error");

			}
		}
	}

	public boolean FileExists(String fname) {
		File file = context.getFileStreamPath(fname);
		return file.exists();
	}
	
	/**
	 * Appends String in line
	 * @param data
	 * @throws IOException
	 */
	public void AppendTo(String data) throws IOException{
		FileUtils.writeStringToFile(file, data, StandardCharsets.UTF_8.name(), true);
	}
	
	/**
	 * Appends String with a new Line afterwards
	 * @param data
	 * @throws IOException
	 */
	public void AppendToNL(String data) throws IOException{
		String out = data + NEWLINE;
		FileUtils.writeStringToFile(file, out, StandardCharsets.UTF_8.name(), true);
	}
	
	public void clearFile() throws IOException{
		FileUtils.writeStringToFile(file, "", StandardCharsets.UTF_8.name(), false);
		Log.e("MainActivity","file cleared: "+ fileName);
	}
	
	public Scanner openFileScanner() throws IOException {
		fileInputStream = FileUtils.openInputStream(file);
		scn = new Scanner(fileInputStream);
		return scn;
	}
	
	public void closeFileScanner() throws IOException {
		scn.close();
		fileInputStream.close();
	}
	
	public void deleteLine(String lineToRemove) {
		StringBuilder sBuilder = new StringBuilder();
		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
			//stream.filter(line->!line.trim().equals(lineToRemove)).forEach(sBuilder::append);
			
			stream.filter(line->!line.trim().equals(lineToRemove)).forEach(	(entry) -> {
               sBuilder.append(entry);                
               sBuilder.append("\n");
            });
			clearFile();
			AppendTo(sBuilder.toString());
			//System.out.println(sBuilder.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void replaceLine(String lineToReplace, String lineReplacement) {
		StringBuilder sBuilder = new StringBuilder();
		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {			
			stream.filter(line->line != null).forEach(	(entry) -> {
				if (entry.equals(lineToReplace)) {
					sBuilder.append(lineReplacement);   
				} else {
					sBuilder.append(entry);   
				}      
                sBuilder.append("\n");
            });
			clearFile();
			AppendTo(sBuilder.toString());
			//System.out.println(sBuilder.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public StringBuilder getText() throws IOException{
		StringBuilder sb = new StringBuilder();
		Scanner lineReader = openFileScanner();
		while (lineReader.hasNext()) {
			sb.append(lineReader.nextLine());
			sb.append("\n");
		}
		closeFileScanner();
		return sb;
	}

}
