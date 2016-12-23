/*
 * Copyright (C) 2016 University of Freiburg.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package rdfanalyzer.spark;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SQLContext;

/**
 * This class is the REST web service which handles front end requests by
 * calling the desired module with specified parameters.
 */
@Path("/ALL")
public class WebService {
	public static SparkConf sparkConf;
	public static JavaSparkContext ctx;
	public static SQLContext sqlContext;
	public static Configuration configuration;

	// TODO: Following comes from Cluster:
	// public static Configuration configuration = new Configuration();
	// public static SparkConf sparkConf =
	// SparkConfigurationHelper.getConfiguration();
	// public static JavaSparkContext ctx = new JavaSparkContext(sparkConf);
	// public static SQLContext sqlContext = new SQLContext(ctx);
	// public static ClassLoader classLoader =
	// WebService.class.getClassLoader();

	@GET
	@Path("/loadGraph/{inputPath}/{inputName}/{inputFormat}")
	public String getMsgg(@PathParam("inputPath") String inputPath, @PathParam("inputName") String inputName,
			@PathParam("inputFormat") boolean inputFormat) {
		String objResponse = "";

		try {
			objResponse = GraphLoader.main(inputPath, inputName, inputFormat);
		} catch (Exception e) {
			objResponse = "Graph Loading Failed!<br>Error Message: " + e.getMessage();
		}

		return objResponse;
	}

	@GET
	@Path("/countEdges/{DataSet}")
	public String getMsg(@PathParam("DataSet") String dataSet) {
		String[] args = { dataSet };
		String objResponse = "";

		try {
			objResponse = CountEdges.main(args);
		} catch (Exception e) {
			objResponse = "Calculation Failed.<br>" + e.getMessage();
		}

		return objResponse;
	}

	@GET
	@Path("/predicateDistribution/{DataSet}/{Type}")
	public String getMsg2(@PathParam("DataSet") String dataSet, @PathParam("Type") String viewType) {
		String[] args = { dataSet, viewType };
		String objResponse = "";

		try {
			objResponse = PredicateDistribution.main(args);
		} catch (Exception e) {
			objResponse = "Calculation Failed.<br>" + e.getMessage();
		}

		return objResponse;
	}

	@GET
	@Path("/countNodes/{DataSet}")
	public String getMsg3(@PathParam("DataSet") String dataSet) {
		String[] args = { dataSet };
		String objResponse = "";

		try {
			objResponse = CountNodes.main(args);
		} catch (Exception e) {
			objResponse = "Calculation Failed.<br>" + e.getMessage();
		}

		return objResponse;
	}

	@GET
	@Path("/getClasses/{DataSet}")
	public String getMsg5(@PathParam("DataSet") String dataSet) {
		String[] args = { dataSet, "Normal" };
		String objResponse = "";

		try {
			objResponse = GetClasses.main(args);
		} catch (Exception e) {
			objResponse = "Calculation Failed.<br>" + e.getMessage();
		}

		return objResponse;
	}

	@GET
	@Path("/collapsedGraph/{DataSet}/{Type}")
	public String getMsg6(@PathParam("DataSet") String dataSet, @PathParam("Type") String Type) throws Exception {
		String[] args = { dataSet, Type };
		String objResponse = "";

		try {
			objResponse = CollapsedGraph.main(args);
		} catch (Exception e) {
			objResponse = "Calculation Failed.<br>" + e.getMessage();
		}

		return objResponse;
	}

	@GET
	@Path("/edgeFinder/{DataSet}/{Type}")
	public String getMsg7(@PathParam("DataSet") String dataSet, @PathParam("Type") String Type) {
		String[] args = { dataSet, Type };
		String objResponse = "";

		try {
			objResponse = EdgeFinder.main(args);
		} catch (Exception e) {
			objResponse = "Calculation Failed.<br>" + e.getMessage();
		}

		return objResponse;
	}

	@GET
	@Path("/inDegree/{DataSet}/{Type}")
	public String getMsg8(@PathParam("DataSet") String dataSet, @PathParam("Type") String Type) {
		String[] args = { dataSet, Type };
		String objResponse = "";

		try {
			objResponse = InDegree.main(args);
		} catch (Exception e) {
			objResponse = "Calculation Failed.<br>" + e.getMessage();
		}

		return objResponse;
	}

	@GET
	@Path("/outDegree/{DataSet}/{Type}")
	public String getMsg9(@PathParam("DataSet") String dataSet, @PathParam("Type") String Type) {
		String[] args = { dataSet, Type };
		String objResponse = "";

		try {
			objResponse = OutDegree.main(args);
		} catch (Exception e) {
			objResponse = "Calculation Failed.<br>" + e.getMessage();
		}

		return objResponse;
	}

	@GET
	@Path("/classDistribution/{DataSet}/{Type}")
	public String getMsg10(@PathParam("DataSet") String dataSet, @PathParam("Type") String viewType) {
		String[] args = { dataSet, viewType };
		String objResponse = "";

		try {
			objResponse = ClassDistribution.main(args);
		} catch (Exception e) {
			objResponse = "Calculation Failed.<br>" + e.getMessage();
		}

		return objResponse;
	}

	@GET
	@Path("/degreeDistribution/{DataSet}/{Type}")
	public String getMsg11(@PathParam("DataSet") String dataSet, @PathParam("Type") String viewType) {
		String[] args = { dataSet, viewType };
		String objResponse = "";

		try {
			objResponse = DegreeDistribution.main(args);
		} catch (Exception e) {
			objResponse = "Calculation Failed.<br>" + e.getMessage();
		}

		return objResponse;
	}

	@GET
	@Path("/countNodesV2/{DataSet}")
	public String getMsg12(@PathParam("DataSet") String dataSet) {
		String[] args = { dataSet };
		String objResponse = "";

		try {
			objResponse = CountNodesV2.main(args);
		} catch (Exception e) {
			objResponse = "Calculation Failed.<br>" + e.getMessage();
		}

		return objResponse;
	}

	@GET
	@Path("/getGraphs")
	public String getMsg12() {
		// TODO: Following is from Cluster version.
		// Maybe we can distribute the JAR file even earlier?
		// ctx.addJar(classLoader.getResource("Project.jar").getFile());
		// SparkConfigurationHelper.setOthers();

		String[] args = {};
		String objResponse = "";

		try {
			objResponse = GetGraphs.main(args);
		} catch (Exception e) {
			objResponse = "Calculation Failed.<br>" + e.getMessage();
		}

		return objResponse;
	}

	@GET
	@Path("/partialRead/{inputSubject}/{inputType}")
	public String getMsg13(@PathParam("inputSubject") String inputSubject, @PathParam("inputType") String inputType) {
		String objResponse = "";

		try {
			if (inputType.equals("edgeFinder")) {
				objResponse = EdgeFinder.partialRead(inputSubject);
			} else {
				objResponse = CollapsedGraph.partialRead(inputSubject);
			}
		} catch (Exception e) {
			objResponse = "Calculation Failed.<br>" + e.getMessage();
		}

		return objResponse;
	}

	@GET
	@Path("/autoComplete/{DataSet}/{UserInput}/{Type}")
	public String getMsg14(@PathParam("DataSet") String dataSet, @PathParam("UserInput") String userInput,
			@PathParam("Type") String Type) {
		String objResponse = "";

		try {
			objResponse = AutoComplete.main(dataSet, userInput, Type);
		} catch (Exception e) {
			objResponse = "Calculation Failed.<br>" + e.getMessage();
		}

		return objResponse;
	}

	// TODO: This is excluded in Cluster version.
	// @GET
	// @Path("/connViewerTest")
	// public String getMsg15() {
	//
	// String objResponse = "";
	//
	// try {
	// ConnAdapter objAdapter = new ConnAdapter();
	// objAdapter.setStartNode("User1");
	// objAdapter.setEndNode("User2");
	// objAdapter.testResult();
	// objResponse = objAdapter.getResults();
	// } catch (Exception e) {
	// objResponse = "Calculation Failed. :" + e.getMessage();
	// }
	//
	// return objResponse;
	// }

	@GET
	@Path("/connViewer/{Node1}/{Node2}/{DataSet}/{Predicates}/{Pattern}")
	public String getMsg16(@PathParam("DataSet") String dataSet, @PathParam("Node1") String startN,
			@PathParam("Node2") String endN, @PathParam("Predicates") String Predicates,
			@PathParam("Pattern") String Pattern) {
		String objResponse = "";

		try {
			startN = startN.replace('$', '/');
			startN = startN.replace('&', '#');
			endN = endN.replace('$', '/');
			endN = endN.replace('&', '#');

			ConnViewer.main(startN, endN, dataSet, Predicates, Pattern);

			objResponse = "Started";
		} catch (Exception e) {
			objResponse = "Calculation Failed.<br>" + e.getMessage();
		}

		return objResponse;
	}

	@GET
	@Path("/connViewerResult")
	public String getMsg17() {
		String objResponse = "";

		try {
			objResponse = ConnViewer.objAdapter.getResults();
		} catch (Exception e) {
			objResponse = "Calculation Failed.<br>" + e.getMessage();
		}

		return objResponse;
	}

	@GET
	@Path("/deleteGraph/{DataSet}")
	public String getMsg18(@PathParam("DataSet") String dataSet) {
		String objResponse = "";

		try {
			objResponse = GetGraphs.deleteGraph(dataSet);
		} catch (Exception e) {
			objResponse = "Calculation Failed.<br>" + e.getMessage();
		}

		return objResponse;
	}

	@GET
	@Path("/closeSession")
	public String getMsg19() {
		String objResponse = "";

		try {
			ctx.close();
			objResponse = "Context Closed.";
		} catch (Exception e) {
			objResponse = "Calculation Failed.<br>" + e.getMessage();
		}

		return objResponse;
	}
}