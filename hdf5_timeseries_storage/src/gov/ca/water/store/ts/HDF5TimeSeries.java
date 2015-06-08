package gov.ca.water.store.ts;

import ncsa.hdf.object.Dataset;
import ncsa.hdf.object.Datatype;
import ncsa.hdf.object.Group;
import ncsa.hdf.object.HObject;
import ncsa.hdf.object.ScalarDS;
import ncsa.hdf.object.h5.H5Datatype;
import ncsa.hdf.object.h5.H5File;
import hec.io.TimeSeriesContainer;

public class HDF5TimeSeries {
	public void storeTimeSeries(TimeSeriesContainer tsc, String file,
			boolean create) throws Exception {
		int openType = H5File.WRITE;
		if (create) {
			openType = H5File.CREATE;
		}

		H5File h5file = new H5File(file, openType);
		int handle = h5file.open();
		// could use fullName or this depending upon what it actually contains
		// to be unique in file
		String groupPath = "/" + tsc.watershed + "/" + tsc.location + "/"
				+ tsc.parameter + "/" + tsc.interval + "/" + tsc.version;
		Group group = createGroupFromPath(groupPath, h5file);
		String name = "/TIMESERIES";
		long[] dims = { tsc.numberValues }; // dimensions 1 x
												// number_of_values
		long[] chunks = { 50000 }; // chunks to read/write
		int gzip = 9; // compression level 1-9
		Datatype valueDataType = new H5Datatype(Datatype.CLASS_FLOAT, 8,
				Datatype.NATIVE, -1);
		Dataset d = h5file.createScalarDS(name, group, valueDataType, dims, null, chunks, gzip, null);
		int dataHandle = d.open();
		d.write(tsc.values);
		d.close(dataHandle);
		h5file.close();

	}
	
	public Group createGroupFromPath(String path, H5File h5file) throws Exception{
		String[] parts = path.split("/");
		Group pGroup=null;
		for(String part: parts){
			if (part.length() > 0){
				pGroup = h5file.createGroup(part, pGroup);
			}
		}
		return pGroup;
	}
	
	public TimeSeriesContainer readTimeSeries(String file, String path) throws Exception{
		TimeSeriesContainer tsc = new TimeSeriesContainer();
		H5File h5file = new H5File(file, H5File.READ);
		int handle = h5file.open();
		HObject hObject = h5file.get(path+"TIMESERIES");
		ScalarDS dataset = (ScalarDS) hObject;
		double [] values = (double[]) dataset.getData();
		tsc.values=values;
		tsc.numberValues = values.length;
		h5file.close();
		return tsc;
	}
}
