package pt.isec.tp.am;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

//import pak.the.magic.R;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Score extends Activity {
	ListView lv;
	static ArrayList<Integer> score = new ArrayList<Integer>();
	static int TOP_SIZE = 10;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.score);

		onLoad(getBaseContext());
		
		lv = (ListView) findViewById(R.id.listViewScore);
		
		lv.setAdapter(new ArrayAdapter<Integer>(this, android.R.layout.simple_list_item_1,score));
	}
	

	static void initScore()
	{
		//inicializa o arraylist das pontuações conforme o tamanho definido por TOP_SIZE
		
		if( score.size() == TOP_SIZE)
			return;
	
		for (int i = score.size()-1; i >= TOP_SIZE; i--)
			score.remove(i);
	
		for(int i = score.size(); i < TOP_SIZE ; i++)
			score.add(new Integer(0));
	}
	
	
	public static void onLoad(Context context) {
		initScore();
			
		 try {

			 BufferedReader input = new BufferedReader(new InputStreamReader(context.openFileInput("Score")));
			 String line="";
			 int i = 0;
				 
			 while((line = input.readLine()) != null) {
				 if ( line.trim().length() == 0 ) continue;
				 if ( i >= TOP_SIZE ) break;
				 score.set(i, Integer.parseInt(line));
				 i++;
		 }
			
		 input.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		 
	  }
	
	
	public static void insertValueOnScore(Integer v) {
		//insere o valor de forma ordenada no arraylist de pontuações.
		
		ArrayList<Integer> aux = new ArrayList<Integer>(); 
		boolean update = false;
		int pos = score.size();
		int i = 0;
		
		for(i=0; i < score.size(); i++) {
			if ( update )
				aux.add(score.get(i));
			else if ( score.get(i).compareTo(v) <= 0 ) {
				update = true;
				aux.add(v);
				aux.add(score.get(i));
				pos = i;
			}
		}
		
		for(i=pos; i < score.size() && (i-pos) < aux.size(); i++) 
			score.set(i, aux.get(i-pos));
	}
	

	
	public static void onSave(Context context, Integer x){
		onLoad(context);
		insertValueOnScore(x);
		
		try {
			BufferedWriter writter = new BufferedWriter(new OutputStreamWriter(
					context.openFileOutput("Score", MODE_WORLD_WRITEABLE)));
			
			for(int i = 0 ; i < score.size();i++) 
				writter.write(score.get(i)+"\n");
			
			writter.close();
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
