package it.polito.tdp.newufosightings.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.newufosightings.db.NewUfoSightingsDAO;

public class Model {

	private NewUfoSightingsDAO dao;
	private Map<String, State> idStateMap;
	private Graph<State, DefaultWeightedEdge> grafo;
	
	public Model() {
		
		this.dao = new NewUfoSightingsDAO();
		
		this.idStateMap = new HashMap<String, State>();
		dao.loadAllStates(idStateMap);
	}
	
	
	public List<String> getShapes(int anno) {
		
		return dao.getShapes(anno);
	}


	public void creaGrafo(int anno, String forma) {
		
		
		this.grafo = new SimpleWeightedGraph<State, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		//carico i vertici
		Graphs.addAllVertices(grafo, idStateMap.values());
		
		//carico gli archi
		dao.loadAllEdges(grafo, idStateMap);
		//carico i pesi
		dao.loadAllWeight(grafo, idStateMap, forma, anno);
		
		System.out.println("Grafo creato! vertici: "+grafo.vertexSet().size()+" archi: "+grafo.edgeSet().size());
		
	}


	public String getGraphConfiguration() {
		
		if(grafo!=null) {
		
		  String ris = "";
		  
		  List<State> stati = new LinkedList<>(idStateMap.values());
		  Collections.sort(stati);
		  for(State stato : stati) {
			  
			  int peso = 0;
			  
			  for(State vicino : Graphs.neighborListOf(grafo, stato)) {
				  if(grafo.containsEdge(grafo.getEdge(stato, vicino)))
					  peso+=grafo.getEdgeWeight(grafo.getEdge(stato, vicino));
			  }
			  
			  ris+=stato+"  peso: "+peso+"\n";
		  }
			
		  return ris;
		  
		  
		}
		
		return null;
	}

}
