package gov.ca.water.store.ts.test;

import static org.junit.Assert.*;

import java.util.Vector;

import gov.ca.water.store.ts.HDF5TimeSeries;
import hec.heclib.dss.HecDSSFileAccess;
import hec.heclib.dss.HecDss;
import hec.hecmath.HecMath;
import hec.io.TimeSeriesContainer;

import org.junit.Test;

public class TimeSeriesContainerStorageTest {

	@Test
	public void testStoreRegularTimeSeries() throws Exception {
		HecDss dss = HecDss.open("test/test1.dss");
		dss.setTimeWindow("01JAN1990 0000", "31DEC2010 2400");
		assertNotNull(dss);
		Vector catalogedPathnames = dss.getCatalogedPathnames("A=FILL+CHAN B=RSAC054 C=STAGE F=DWR-DMS-201203_NAVD");
		assertNotNull(catalogedPathnames);
		assertTrue(catalogedPathnames.size()>0);
		String pathname = (String) catalogedPathnames.elementAt(0);
		TimeSeriesContainer tsc = (TimeSeriesContainer) dss.get(pathname);
		assertNotNull(tsc);
		//
		HDF5TimeSeries storer = new HDF5TimeSeries();
		storer.storeTimeSeries(tsc, "test/test1.hdf5", true);
		//
		HDF5TimeSeries hdf5reader = new HDF5TimeSeries();
		int niterations=100;
		System.out.println("Reading 15min data of length "+tsc.numberValues+" "+ niterations + " times");
		long ti=System.currentTimeMillis();
		for(int i=0; i < niterations; i++){
			TimeSeriesContainer hdf5ts = hdf5reader.readTimeSeries("test/test1.hdf5", "/FILL+CHAN/RSAC054/STAGE/15/DWR-DMS-201203_NAVD/");
			assert hdf5ts.numberValues == tsc.numberValues;
		}
		System.out.println("Average time to read HDF5: " + (1.*(System.currentTimeMillis()-ti))/niterations);

		HecDSSFileAccess.setMessageLevel(0);
		ti=System.currentTimeMillis();
		for(int i=0; i < niterations; i++){
			dss = HecDss.open("test/test1.dss");
			dss.setTimeWindow("01JAN1990 0000", "31DEC2010 2400");
			TimeSeriesContainer dssts = (TimeSeriesContainer) dss.get(pathname);
			assert dssts.numberValues == tsc.numberValues;
			dss.close();
		}
		System.out.println("Average time to read DSS: " + (1.*(System.currentTimeMillis()-ti))/niterations);
	}

}
