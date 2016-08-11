package jobs.steps

import java.util.Iterator;
import java.util.List;

import au.com.bytecode.opencsv.CSVWriter

import com.google.common.base.Function
import com.google.common.collect.Iterators
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.RegularImmutableMap

import jobs.table.BackingMap
import jobs.table.Table

import org.mapdb.Fun
import org.transmartproject.core.exceptions.EmptySetException

class SysinflameSimpleDumpTableResultStep extends AbstractDumpStep {

	Table table
	List<String> outList = new LinkedList<String>()
	File temporaryDirectory
	LinkedList header = new LinkedList<String>()
	final String statusName = 'Dumping Table Result'

	@Override
	void execute() {
		try {
			withDefaultCsvWriter { CSVWriter it ->
				gatherData()
				writeHeader it
				
				writeMeat it
			}
		} finally {
			table.close()
		}
	}

	protected List<String> getHeaders() {
		table.headers
	}
	
	
	

	protected Iterator getMainRows() {
		table.result.iterator()
	}

	void writeHeader(CSVWriter writer) {
		writer.writeNext(getHeader() as String[])
	}

	void gatherData() {
		//1102, [[key,value],[key2,value2]]
		HashMap<String, LinkedList<HashMap<String,Object>>> newMap = new HashMap<String, LinkedList<HashMap<String,Object>>>()

		List patients = new LinkedList<String>()
		header.add("PATIENTID")
		def rows = getMainRows()
		if (!rows.hasNext()) {
			throw new EmptySetException("The result set is empty. " +
			"Number of patients dropped owing to mismatched " +
			"data: ${table.droppedRows}")
		}
		rows.each {
			ArrayList inputArray = it
			String patID = inputArray.get(0)
			if (!patients.contains(patID))
				patients.add(patID)
			Map<String, Object> arrayMap = inputArray.get(1)
			arrayMap.each {
				String toAdd = it.key;
				while (toAdd.startsWith("\\"))
					toAdd = toAdd.substring(1)
				while (toAdd.endsWith("\\"))
					toAdd = toAdd.substring(0, toAdd.length()-1)
				if (!header.contains(toAdd))
					header.add(toAdd)

				HashMap<String, Object> tmp = new HashMap<String, Object>()
				tmp.put(toAdd,it.value)
				if (newMap.containsKey(patID)){
					newMap.get(patID).add(tmp)
				}
				else {
					LinkedList<HashMap<String,Object>> newList = new LinkedList<HashMap<String,Object>>()
					newList.add(tmp)
					newMap.put(patID,newList)
				}
			}
		}

		patients.each {
			String patID = it
			List<String> out = new LinkedList<String>()
			out.add(patID)
			header.each {
				String headerString = it
				newMap.get(patID).each {
					if (it.containsKey(headerString)){
						out.add(it.get(headerString))
					}
					else if (header.equals('PATIENTID')){
						//out.add(patID)
					}
				}
			}
			outList.add(out)
		}
	}

	void writeMeat(CSVWriter writer) {
		outList.each {
			writer.writeNext(it as String[])
		}
	}

	private void withDefaultCsvWriter(Closure constructFile) {
		File output = new File(temporaryDirectory, outputFileName)
		output.withWriter { writer ->
			CSVWriter csvWriter = new CSVWriter(writer, '\t' as char)
			constructFile.call(csvWriter)
		}
	}
}
