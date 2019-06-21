package it.polito.tdp.newufosightings.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.newufosightings.model.Sighting;
import it.polito.tdp.newufosightings.model.State;

public class NewUfoSightingsDAO {

	public List<Sighting> loadAllSightings() {
		String sql = "SELECT * FROM sighting";
		List<Sighting> list = new ArrayList<>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);	
			ResultSet res = st.executeQuery();

			while (res.next()) {
				list.add(new Sighting(res.getInt("id"), res.getTimestamp("datetime").toLocalDateTime(),
						res.getString("city"), res.getString("state"), res.getString("country"), res.getString("shape"),
						res.getInt("duration"), res.getString("duration_hm"), res.getString("comments"),
						res.getDate("date_posted").toLocalDate(), res.getDouble("latitude"),
						res.getDouble("longitude")));
			}

			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}

		return list;
	}

	public void loadAllStates(Map<String, State> idStateMap) {
		
		String sql = "SELECT * FROM state";
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				
				if(!idStateMap.containsKey(rs.getString("id"))) {
				
				State state = new State(rs.getString("id"), rs.getString("Name"), rs.getString("Capital"),
						rs.getDouble("Lat"), rs.getDouble("Lng"), rs.getInt("Area"), rs.getInt("Population"),
						rs.getString("Neighbors"));
				
				idStateMap.put(rs.getString("id"), state);
			
				
				}
			}

			conn.close();
			

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<String> getShapes(int anno) {
		
       String sql = "SELECT shape " + 
       		"FROM sighting " + 
       		"WHERE YEAR(DATETIME) = ? " + 
       		"GROUP BY shape";
       
       List<String> result = new ArrayList<>();
       
       try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				
				result.add(rs.getString("shape"));
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		
	}

	public void loadAllEdges(Graph<State, DefaultWeightedEdge> grafo, Map<String, State> idStateMap) {
		
		String sql = "SELECT * FROM neighbor WHERE state2>state1";
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				
				if(idStateMap.containsKey(rs.getString("state1")) && idStateMap.containsKey(rs.getString("state2"))) {
					
					grafo.addEdge(idStateMap.get(rs.getString("state1")), idStateMap.get(rs.getString("state2")));
					
				}
			}

			conn.close();
			

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		
	}

	public void loadAllWeight(Graph<State, DefaultWeightedEdge> grafo, Map<String, State> idStateMap, String forma,
			int anno) {
		
		String sql = "SELECT  n.state1, n.state2, COUNT(*) AS peso " + 
		       		"FROM sighting s1, sighting s2, neighbor n " + 
		       		"WHERE YEAR(s1.DATETIME) = ? and YEAR(s1.DATETIME) = YEAR(s2.DATETIME) " + 
		       		"AND ((s1.state = n.state1 AND s2.state = n.state2) OR (s2.state = n.state1 AND s1.state = n.state2)) " + 
		       		"AND n.state2>n.state1 " + 
		       		"AND s1.shape = ? AND s2.shape = s1.shape " + 
		       		"GROUP BY n.state1, n.state2 ";
				
				try {
					Connection conn = ConnectDB.getConnection();
					PreparedStatement st = conn.prepareStatement(sql);
					st.setInt(1, anno);
					st.setString(2,forma);
					ResultSet rs = st.executeQuery();

					while (rs.next()) {
						
						if(idStateMap.containsKey(rs.getString("n.state1")) && idStateMap.containsKey(rs.getString("n.state2"))) {
							
							if(grafo.containsEdge(grafo.getEdge(idStateMap.get(rs.getString("n.state1")), 
									idStateMap.get(rs.getString("n.state2"))))) {
								grafo.setEdgeWeight(grafo.getEdge(idStateMap.get(rs.getString("n.state1")), 
									idStateMap.get(rs.getString("n.state2"))), rs.getInt("peso"));
							}

							
						}
					}

					conn.close();
					

				} catch (SQLException e) {
					e.printStackTrace();
					System.out.println("Errore connessione al database");
					throw new RuntimeException("Error Connection Database");
				}
				
		
	}

}
