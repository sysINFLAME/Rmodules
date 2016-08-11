/*************************************************************************
 * Copyright 2008-2012 Janssen Research & Development, LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************/

package com.recomdata.transmart.data.association

/**
 *@Author Benjamin Baum, Department of Medical Informatics, Göttingen, <benjamin.baum@med.uni-goettingen.de>
 */
class SysinflameOutlierController {


	def RModulesOutputRenderService

	def sysinflameOutlierOutput =
	{
		//This will be the array of image links.
		def ArrayList<String> imageLinks = new ArrayList<String>()

		//This will be the array of text file locations.
		def ArrayList<String> txtFiles = new ArrayList<String>()

		//Grab the job ID from the query string.
		String jobName = params.jobName
		//Gather the image links.
		RModulesOutputRenderService.initializeAttributes(jobName,"sysinflameOutlier",imageLinks)
		String tempDirectory = RModulesOutputRenderService.tempDirectory
		log.info("jobName: " + jobName)
		log.info(imageLinks)
		//Traverse the temporary directory for the LinearRegression files.
		def tempDirectoryFile = new File(tempDirectory)
		String correlationLocation = "${tempDirectory}" + File.separator + "Ausgabe.csv"
		String outlierData = RModulesOutputRenderService.fileParseLoop(tempDirectoryFile,/.*Ausgabe.*\.csv/,/.*Ausgabe.*\.csv/,parseCorrelationFile)
		render(template: "/plugin/sysinflameOutlier_out", 
			model:[outlierData:outlierData,
				imageLocations:imageLinks,
				zipLink:RModulesOutputRenderService.zipLink],
			contextPath:pluginContextPath)

	}
	def parseCorrelationFile = {
		inStr ->
		
		StringBuffer buf = new StringBuffer();
		//Create the opening table tag.
		buf.append("<table class='AnalysisResults'>")
		//This is a line counter.
		Integer lineCounter = 0;
		//This is the string array with all of our variables.
		String[] variablesArray = []
		int columns = 0;
		inStr.eachLine {
			println it;
			//The first line has a list of the variables.
			if(lineCounter == 0) {
				variablesArray = it.split(";");
				columns = variablesArray.length
				//Write the variable names across the top.
				variablesArray.each {
					currentVar ->
					String printableHeading = currentVar
					buf.append("<th>${printableHeading}</th>")
				}
				//Close header row.
				buf.append("</tr>")
			} else {
				String[] strArray = it.split(";");
				buf.append("<tr>");
				int rowColumn = 0;
				strArray.each {
					currentValue ->
					//add currentValue to the table
					buf.append("<td>${currentValue}</td>")
					rowColumn++
					}
				//add empty columns depending on tableheader
				while (rowColumn < columns) {
				buf.append("<td></td>")
				rowColumn++
				}
				//Close header row.
				buf.append("</tr>");
			}
			lineCounter+=1
		}
		buf.append("</table>");
		return buf.toString();
	}

}
