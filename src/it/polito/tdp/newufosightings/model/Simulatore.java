package it.polito.tdp.newufosightings.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

public class Simulatore {

	private static final int DEFCON_MAX = 5;
	private static final int DEFCON_MIN = 1;
	
	private PriorityQueue<Evento> queue;
	
	
	//ho usato tre mappe ma potevo usare tre attributi
	
    private Map<String, Integer> mappaAllerta;
    private Map<String, Boolean> isInAllerta;    
    private Map<String, Integer> saltaAllerta;
    
    private Graph<State, DefaultWeightedEdge> grafo;
    
    /*
     * Se uno stato è già in allerta quando riceve una segnalazione
     * deve saltare il prossimo cessa allerta, complessa come cosa 
     */
    
    
    private Duration intervalloAllerta;
    private LocalDateTime ultimo; 
    private double alfa;
    
    
    public void init(double alfa, int intervalloAllerta, List<Evento> eventi, Set<String> stati, 
    		Graph<State, DefaultWeightedEdge> grafo) {
    	
    	this.mappaAllerta = new HashMap<String, Integer>();
    	this.isInAllerta = new HashMap<String, Boolean>();
    	this.saltaAllerta = new HashMap<>();
    	
    	for(String stato : stati) {
    		mappaAllerta.put(stato, DEFCON_MAX);
    		isInAllerta.put(stato, false);
    		saltaAllerta.put(stato, 0);
    		}
    	
    	this.queue = new PriorityQueue<Evento>(eventi);
    	this.intervalloAllerta = Duration.ofDays(intervalloAllerta);
    	this.alfa = alfa/100; //lo rendo percentuale
    	this.grafo = grafo;
    	//abbiamo gli eventi ed i parametri
    	
    	this.ultimo = eventi.get(eventi.size()-1).getData();
    	
    		
    	
    }
    
    
   public void run() {
	   
	   /*
	    * Ogni volta che pesco un evento 
	    */
	   
	   
	   while(!queue.isEmpty() && queue.peek().getData().compareTo(ultimo)<=0) {
		   
		   Evento ev = queue.poll();
		   
		   switch(ev.getTipo()) {
		   
		   case AVVISTAMENTO:
			   
			   //valuto il livello di allerta
			   if(mappaAllerta.get(ev.getIdState())>DEFCON_MIN) {
				   int prima = mappaAllerta.get(ev.getIdState());
				   mappaAllerta.put(ev.getIdState(), prima-1);
				   if(isInAllerta.get(ev.getIdState())) {
					   int first = saltaAllerta.get(ev.getIdState());
					   saltaAllerta.put(ev.getIdState(), first+1);
				   }
					   
			   }
			   
			 break;
		   case CESSATA_ALLERTA:
			 break;
		   
		   }
		   
	   }
	   
   }
}
