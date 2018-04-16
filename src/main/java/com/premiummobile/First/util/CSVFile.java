package com.premiummobile.First.util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.web.multipart.MultipartFile;

public class CSVFile {

	private Map<String, ArrayList<String>> columns;
	private Map<String, List<String>> rows;
	private int columnsSize;
	private int rowsSize;
	
	public CSVFile(MultipartFile file, String separator, boolean firstRowHeaders, String fieldEnclosure, String rowIdentifier, boolean removeDuplicates) throws UnsupportedEncodingException, IOException{
		fieldEnclosure = fieldEnclosure != null ? fieldEnclosure : "";
		columns = new HashMap<String, ArrayList<String>>();
		rows = new HashMap<String, List<String>>();
		String completeData = new String(file.getBytes(),"UTF-8").trim();
		String[] rowsArray = completeData.split("\\r?\\n");
		if(rowsArray.length == 0){
			throw new IOException("Empty file.");
		}
		List<String> rowsList = new ArrayList<>();
		Collections.addAll(rowsList, rowsArray);
		String[] fields = rowsList.get(0).split(separator);
		
		if(firstRowHeaders){
	        rowsList.remove(0);
	        rowsSize = rowsList.size();
	        for(int i = 0; i < fields.length; i++){
				columns.put(fields[i].replaceAll(fieldEnclosure, "").trim(), new ArrayList<String>());
			}
	        if(rowIdentifier != null){
	        	if(!columns.containsKey(rowIdentifier)){
	        		throw new IOException(rowIdentifier + ": There is no such column.");
	        	}
	        }
		}
		else{
			for(int i = 0; i < fields.length; i++){
				columns.put("Column" + i, new ArrayList<String>());
			}
			rowIdentifier = "Column0";
		}
		
		columnsSize = columns.size();
		
		int rowCount = 0;
		
		for(String row : rowsList){
			String[] values = row.split(separator);

			int counter = 0;
			
			System.out.println(rowCount++);
			if(values.length == columnsSize){
				for(ArrayList<String> entry : columns.values()){
					entry.add(values[counter]);
					counter++;
				}
			}
			else{
				throw new IOException("Row " + rowCount + ": The size of the columns of this row is not equal to first row column size");
			}
			
		}
		
		int counter = 0;
		HashMap<String, String[]> temp = new HashMap<String, String[]>();
		if(removeDuplicates){
			for(String entry : columns.get(rowIdentifier)){
				String[] values = rowsList.get(counter).split(separator);
				temp.put(entry, values);
			}
			for(Entry<String, String[]> entry : temp.entrySet()){
				rows.put(entry.getKey(), Arrays.asList(entry.getValue()));
			}
		}
		else{
			for(String entry : columns.get(rowIdentifier)){
				String[] values = rowsList.get(counter).split(separator);
				rows.put(entry, Arrays.asList(values));
			}
		}
		counter++;
		
	}
	
	public List<String> getColumn(String column){
		return columns.get(column);
	}
	
	public List<String> getRow(String row){
		return rows.get(row);
	}
	
	public Map<String, List<String>> getRows(){
		return rows;
	}
	public String getValueByColumnAndRow(String column, int row){
		return columns.get(column).get(row);
	}
	
	public Map<String, String> getRow(int index){
		Map<String, String> row = new HashMap<String, String>();
		for(Entry<String, ArrayList<String>> entry : columns.entrySet()){
			row.put(entry.getKey(), entry.getValue().get(index));
		}
		return row;
	}
	
	public Map<String, String> getRowByFieldValuePair(String columnName, String entryValue){
		Map<String, String> row = new HashMap<String, String>();
		ArrayList<String> column = columns.get(columnName);
		int rowIndex = 0;
		for(int i = 0; i < column.size(); i++){
			if(column.get(i).equals(entryValue)){
				rowIndex = i;
				break;
			}
		}
		for(Entry<String, ArrayList<String>> entry : columns.entrySet()){
			row.put(entry.getKey(), entry.getValue().get(rowIndex));
		}
		return row;
	}
	
	public int getRowsSize(){
		return rowsSize;
	}
	
	public int getColumnsSize(){
		return columnsSize;
	}
}
