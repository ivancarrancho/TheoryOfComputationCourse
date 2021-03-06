/*
# Solution for a class exercise.
#
# Created by Msc. Carlos Andres Sierra on February 2017.
# Copyright (c) 2017  Msc. Carlos Andres Sierra. Research Group on Artificial Life - ALIFE. All rights reserved.
#
# This file is part of TheoryOfComputation course.
#
# TheoryOfComputationCourse is free software: you can redistribute it and/or modify it under the terms of the
# GNU General Public License as published by the Free Software Foundation, version 2.
*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Vector;

/**
 * This class represents the behavior of a Standard Turing Machine
 * @author MSc. Carlos Andres Sierra, PhD. student
 */
public class TuringMachine_standard {

	//Dynamic vector saves all transitions of any Turing Machine
	private Vector<String[]> transitions = null;
	
	
	/**
	 * Constructor zero-parameters of the class
	 */
	public TuringMachine_standard() 
	{
		//Instances the Transitions Vector
		this.transitions = new Vector<String[]>();
	}
	
	
	/**
	 * This method is used to add a new transition to current Transitions Vector
	 * @param newTransition
	 */
	public void addTransition(String newTransition)
	{
		String[] transition = newTransition.split(","); //Separate all fields by the colon
		this.transitions.add(transition); //Add the new transition to the Transitions Vector
	}

	/**
	 * This method is used to simulate a process of an input in the Turing Machine
	 * @param input
	 * @return True = accepted, False = rejected
	 */
	public void process(String input)
	{
		boolean result = false; //Initial assumption
		
		String log = ""; //This log saves all movements that Turing Machine made
		String current_state = "q0"; //Initial state of the machine
		int index = 1; //Initial position of read-write unit over the tape
		
		String[] tape = new String[input.length() + 2]; //Create a tape, with blank symbols at the beginning and the end, and the input in the middle.
		//If you need, you can add many blank symbols as you want. For many of all examples of our course, you do not need to add more blank symbols.
		tape[0] = tape[ tape.length - 1 ] = "blank"; //Add blanks at first and last position
		
		for(int i = 1; i < (tape.length - 1); i++) //Move trough the input
			tape[i] = input.charAt(i - 1)+""; //Add input to the tape
		
		//Real process start here
		while(!current_state.equals("qf") || !tape[index].equals("blank")) //While Turing Machine isn't in FinalState or reading 'blank', it must to continue processing over the tape 
		{
			//Add movement to log
			log += ("State: " + current_state + "\tSymbol: " + tape[index] + "\tIndex: " + index + "\n");
			
			int i; //Variable used like index to move trough Transitions Vector
			for(i = 0; i < this.transitions.size(); i++) //Move over all the transitions
				if(this.transitions.get(i)[0].equals(current_state)) //If the current state is the same that state in transition
					if(this.transitions.get(i)[1].equals( tape[index] )) //If symbol in tape is the same that symbol in transition
					{
						//If the process get here, is cos' found a valid transition
						current_state = this.transitions.get(i)[2]; //New current state
						tape[index] = this.transitions.get(i)[3]; //Overwrite symbol in the tape
						
						//If the transition specifies that the read-write unit must move forward, index must increase in one position
						if(this.transitions.get(i)[4].equals(">"))
							index += 1;
						else //If the transition specifies that the read-write unit must move backward, index must decrease in one position
							if(this.transitions.get(i)[4].equals("<"))
								index -= 1;
		
						//If the transition specifies that the read-write unit must not move, index don't be modified
						
						//Because the right transition has been found, Machine don't need to search more transitions...so, we can break the search and leave at machine made next step
						break;
					}
			
			//If this conditional is true, is due to not exists any valid transition	
			if(i == this.transitions.size())
				break;
		}
		
		//Add movement to log 
		log += ("State: " + current_state + "\tSymbol: " + tape[index] + "\tIndex: " + index + "\n");
		
		if(current_state.equals("qf") && tape[index].equals("blank")) //Process is successful if Turing Machine is on FinalState and the read-write unit is reading a blank symbol
			result = true; //Compute successful
		
		//Create a way to print in console
		BufferedWriter bw = new BufferedWriter( new OutputStreamWriter( System.out ) );
		try 
		{
			bw.write(log); //Add to print a log of Turing Machine movements
			
			//Evaluate the input with Turing Machine
			if( result ) //Invoke a process in the machine
				bw.write("\nComputo terminado"); //Input leave at machine to finish in a FinalState
			else
				bw.write("\nComputo rechazado"); //Input don't leave at machine to finish in a FinalState
			
			bw.flush(); //Print in the console
			bw.close(); //Close buffer
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
	}
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		String file_transitions = ""; //Name of the file that saves transition function
		String w = ""; //Input of the machine
		
		//Read from console
		BufferedReader console = new BufferedReader( new InputStreamReader( System.in ) );
		
		try 
		{
			file_transitions = console.readLine(); //Request to user the file path
			w = console.readLine(); //Request to user the input
		} 
		catch (IOException e1) 
		{
			e1.printStackTrace();
		}
		
		//Create Turing Machine
		TuringMachine_standard M = new TuringMachine_standard();
		
		//Create file access	 handler
		File f = new File(file_transitions);
		try 
		{
			FileReader fr = new FileReader( f ); //Create file reader
			BufferedReader br = new BufferedReader( fr ); //Read from file
			
			String line = br.readLine(); //Read the first line of file
			
			//While exists line with transitions
			while(line != null)
			{
				M.addTransition(line); //Add transition to Turing Machine
				line = br.readLine(); //Read next line of the file
			}
			
			//Evaluate the input with Turing Machine
			M.process(w); //Invoke a process in the machine
			
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}
