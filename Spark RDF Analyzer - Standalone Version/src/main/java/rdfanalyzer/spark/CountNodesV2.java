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

import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;

public class CountNodesV2 {
	public static String main(String[] args) throws Exception {

 		/*
		 * Check if arguments have been passed.
		 */
		 if(args.length!=1)
		   {
			   System.out.println("Missing Arguments <INPUT>");
			   System.exit(0);
		   }
		 
		 	/*
			 * Read graph from parquet 
			 */
		   DataFrame schemaRDF = WebService.sqlContext.parquetFile(Configuration.properties.getProperty("Storage")+args[0]+".parquet");
		   schemaRDF.cache().registerTempTable("Graph");


	   // SQL can be run over RDDs that have been registered as tables.
	   DataFrame predicatesFrame = WebService.sqlContext.sql("SELECT COUNT(DISTINCT MyTable1.subject) FROM (SELECT subject FROM Graph"
		   		+ " UNION ALL SELECT object FROM Graph WHERE object NOT LIKE '\"%' "
		   		+ " ) MyTable1");

	   // The results of SQL queries are DataFrames and support all the normal RDD operations.
	   // Save result to file   
	   Row[] rows = predicatesFrame.collect(); 
	   String Literals = Long.toString(rows[0].getLong(0));
	   
	   // SQL can be run over RDDs that have been registered as tables.
	   predicatesFrame = WebService.sqlContext.sql("SELECT COUNT(object) FROM Graph WHERE object LIKE '\"%'");

	   // The results of SQL queries are DataFrames and support all the normal RDD operations.
	   // Save result to file   
	   rows = predicatesFrame.collect();
	   
	   String Objects = Long.toString(rows[0].getLong(0));
	   
	   String Sum = Integer.toString(Integer.parseInt(Literals)+Integer.parseInt(Objects));
	   
	   return "<h3><span class=\"glyphicon glyphicon-file\" aria-hidden=\"true\"></span>&nbsp;Objects: "+Objects+"</h3><h3><span class=\"glyphicon glyphicon-menu-hamburger\" aria-hidden=\"true\"></span>&nbsp;Literals: "+Literals+"</h3><h3><span class=\"glyphicon glyphicon-globe\" aria-hidden=\"true\"></span>&nbsp;All: "+Sum+"</h3>";

	   }
}