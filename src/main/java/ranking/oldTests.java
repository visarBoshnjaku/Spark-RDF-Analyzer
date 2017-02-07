package ranking;

import java.util.ArrayList;
import java.util.List;

import rdfanalyzer.spark.Service;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.rdd.RDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.storage.StorageLevel;
import org.graphframes.GraphFrame;
import org.apache.spark.graphx.*;
import org.apache.spark.graphx.lib.*;

import scala.Tuple2;
import scala.collection.immutable.Map;
import scala.collection.immutable.Seq;

public class oldTests {
	
	public List<Tuple2<Object,String>> vertices = new ArrayList<>();
	public List<Edge<String>> edges = new ArrayList<>();

	public class User {
		private String id;
		private String name;
		private int age;

		public User(){      
		}

		public User(String id, String name, int age) {
		    super();
		    this.id = id;
		    this.name = name;
		    this.age = age;
		}

		public String getId() {
		    return id;
		}
		public void setId(String id) {
		    this.id = id;
		}
		public String getName() {
		    return name;
		}
		public void setName(String name) {
		    this.name = name;
		}
		public int getAge() {
		    return age;
		}
		public void setAge(int age) {
		    this.age = age;
		 }
		}
	
	public class Relation {

		private String src;
		private String dst;
		private String relationship;

		public Relation(){

		}

		public Relation(String src, String dst, String relationship) {
		    super();
		    this.src = src;
		    this.dst = dst;
		    this.relationship = relationship;
		}

		public String getSrc() {
		    return src;
		}

		public void setSrc(String src) {
		    this.src = src;
		}

		public String getDst() {
		    return dst;
		}

		public void setDst(String dst) {
		    this.dst = dst;
		}

		public String getRelationship() {
		    return relationship;
		}

		public void setRelationship(String relationship) {
		    this.relationship = relationship;
		  }

		}
	public void test(){
		
		   List<User> uList = new ArrayList<User>() {
		        {
		            add(new User("a", "Alice", 34));
		            add(new User("b", "Bob", 36));
		            add(new User("c", "Charlie", 30));
		        }
		    };

		    Dataset<Row> verDF = Service.spark().sqlContext().createDataFrame(uList, User.class);

		    //Create an Edge DataFrame with "src" and "dst" columns
		    List<Relation> rList = new ArrayList<Relation>() {
		        {
		            add(new Relation("a", "b", "friend"));
		            add(new Relation("b", "c", "follow"));
		            add(new Relation("c", "b", "follow"));
		        }
		    };

		    Dataset<Row> edgDF = Service.spark().sqlContext().createDataFrame(rList, Relation.class);

		    //Create a GraphFrame
		    GraphFrame gFrame = new GraphFrame(verDF, edgDF);
		    //Get in-degree of each vertex.
		    //Count the number of "follow" connections in the graph.
		    long count = gFrame.edges().filter("relationship = 'follow'").count();
		    //Run PageRank algorithm, and show results.
//		    PageRank pRank = gFrame.pageRank().resetProbability(0.01).tol(0.5);
//		    pRank.run().vertices().select("id", "pagerank").show();
		    
		    org.graphframes.lib.ShortestPaths path = new org.graphframes.lib.ShortestPaths(gFrame);
		    ArrayList<Object> obj = new ArrayList<>();
		    obj.add("c");
		    obj.add("b");
		    path.landmarks(obj);
		    path.run().show();
		    
	}
	
	
	
	public void ShortestPaths(){
		
		
		createVertices();
		createEdges();
		System.out.println("creating");
		
		JavaRDD<Tuple2<Object,String>> distData = Service.sparkCtx().parallelize(vertices);
		JavaRDD<Edge<String>> edgeData = Service.sparkCtx().parallelize(edges);

		RDD<Tuple2<Object,String>> counters= JavaRDD.toRDD(distData);
		RDD<Edge<String>> edgeCounters= JavaRDD.toRDD(edgeData);
		System.out.println("created datasets");

		List<Object> longIds = new ArrayList<>();
		longIds.add(1L);
		longIds.add(5L);

		Seq<Object> s = scala.collection.JavaConversions.asScalaBuffer(longIds).toList().toSeq();

		System.out.println("created seq object counter");
		
		System.out.println("created seq object");
		
		Graph<String, String> graph = Graph.apply(
				counters,
				edgeCounters, 
				"",
				StorageLevel.MEMORY_AND_DISK(),
				StorageLevel.MEMORY_AND_DISK(),
				scala.reflect.ClassTag$.MODULE$.apply("".getClass()),
				scala.reflect.ClassTag$.MODULE$.apply("".getClass()));
		

		System.out.println("number of edges in graph = "+graph.edges().count());

		System.out.println("number of vertices in graph = "+graph.vertices().count());
		
		 System.out.println("created the graph");
		 Graph<scala.collection.immutable.Map<Object,Object>,String> shortestpaths = 
				 ShortestPaths.run(graph, s,scala.reflect.ClassTag$.MODULE$.apply("".getClass()));
		
		 System.out.println("applied shortest path");

		 JavaRDD<Tuple2<Object,Map<Object,Object>>> vert =  shortestpaths.vertices().toJavaRDD();
		 System.out.println("converting short path to rdd");
		
//		
//		vert.map(new Function<Tuple2<Object,Map<Object,Object>>, Tuple2<Object,Map<Object,Object>>>() {
//
//			@Override
//			public Tuple2<Object, Map<Object, Object>> call(Tuple2<Object, Map<Object, Object>> arg0) throws Exception {
//				
//				System.out.println("This is the parent -> " + (String)arg0._1);
//
//				Iterator<Tuple2<Object,Object>> iter = arg0._2.iterator();
//
//				while (iter.hasNext())
//				{
//				    System.out.println("mapKey = "+(String)iter.next()._1 + " MapValue = "+(String)iter.next()._2);
//				}
//				
//				return arg0;
//			}
//		});

	}

	private  void createVertices(){
		vertices.add(new Tuple2<Object,String>(1L,"node1"));
		vertices.add(new Tuple2<Object,String>(2,"node2"));
		vertices.add(new Tuple2<Object,String>(3,"node3"));
		vertices.add(new Tuple2<Object,String>(4,"node4"));
		vertices.add(new Tuple2<Object,String>(5,"node5"));
	}
	
	private void createEdges(){
		
		edges.add(new Edge<String>(1,2,"edge12"));
		edges.add(new Edge<String>(1,3,"edge13"));
		edges.add(new Edge<String>(1,4,"edge14"));
		
		edges.add(new Edge<String>(2,1,"edge21"));
		edges.add(new Edge<String>(2,5,"edge25"));

		edges.add(new Edge<String>(3,2,"edge32"));
		edges.add(new Edge<String>(3,4,"edge34"));
		edges.add(new Edge<String>(3,1,"edge31"));

		edges.add(new Edge<String>(4,1,"edge41"));
		edges.add(new Edge<String>(4,2,"edge42"));

		edges.add(new Edge<String>(5,4,"edge54"));
		edges.add(new Edge<String>(5,3,"edge53"));
		edges.add(new Edge<String>(5,2,"edge52"));
		
}
}
