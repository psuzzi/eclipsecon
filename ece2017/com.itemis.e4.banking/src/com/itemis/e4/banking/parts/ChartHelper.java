package com.itemis.e4.banking.parts;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.nebula.visualization.xygraph.dataprovider.CircularBufferDataProvider;
import org.eclipse.nebula.visualization.xygraph.dataprovider.Sample;
import org.eclipse.nebula.visualization.xygraph.figures.Trace;
import org.eclipse.nebula.visualization.xygraph.figures.Trace.PointStyle;
import org.eclipse.nebula.visualization.xygraph.figures.XYGraph;

import com.itemis.e4.banking.model.Account;
import com.itemis.e4.banking.model.Transaction;

/** Helper to buidl charts */
public class ChartHelper {
	
	public static void generateGraph(LightweightSystem lws, Account account) {
		XYGraph xyGraph = new XYGraph();
		xyGraph.setBackgroundColor(ColorConstants.white);
		lws.setContents(xyGraph);

		xyGraph.setTitle("Balance chart");
		xyGraph.getPrimaryXAxis().setTitle("days");
		xyGraph.getPrimaryXAxis().setAutoScale(true);
		xyGraph.getPrimaryXAxis().setFormatPattern("0");
		xyGraph.getPrimaryYAxis().setTitle("amount");
		xyGraph.getPrimaryYAxis().setAutoScale(true);
		xyGraph.getPrimaryYAxis().setFormatPattern("#.00");
		
		// create the trace
//		CircularBufferDataProvider provider = new CircularBufferDataProvider(true);
//		provider.setBufferSize(100);
//		if (account != null) {
//			System.out.println("Add Samples");
//			provider.addSample(new Sample(20, 30));
//			provider.addSample(new Sample(200, 300));
//			provider.addSample(new Sample(2000, 3000));
//			double dayToday = (new Date().getTime() / (1000 * 60 * 60 * 24));
//			double dayCreated = (account.getCreationDate().getTime() / (1000 * 60 * 60 * 24));
//			double dayTransaction;
//			//
//			// ç
//			List<Transaction> transactions = account.getTransactions().stream()
//					.filter(t->t.isProcessed())
//					.sorted((t1,t2)->t2.getProcessedDate().compareTo(t1.getProcessedDate()))
//					.collect(Collectors.toList());
//			//
//			double currentAmount = account.getBalance();
//			double maxDay = dayToday - dayCreated;
//			double minDay = 0;
//			// first item, today's balance
//			provider.addSample(new Sample(maxDay, currentAmount));
//			for (Transaction t : transactions) {
//				// transaction time in days until today
//				dayTransaction = (double) ((t.getProcessedDate().getTime() / (1000 * 60 * 60 * 24))-dayCreated);
//				// change in the amount due to the transaction
//				boolean incomingTransfer = !t.getSourceAccount().equals(account);
//				currentAmount -= (incomingTransfer) ? t.getRecipientAccountDeltaAmount() : t.getSourceAccountDeltaAmount();
//				// 
//				provider.addSample(new Sample(dayTransaction, currentAmount));
//			}
//			// add last item: today
//			provider.addSample(new Sample(minDay, currentAmount));
//		}
//		Trace trace = new Trace("Trace1", xyGraph.getPrimaryXAxis(), xyGraph.getPrimaryYAxis(), provider);
//		trace.setPointStyle(PointStyle.CIRCLE);
//		trace.setLineWidth(2);
//		// add the trace to xyGraph
//		xyGraph.addTrace(trace);
//		xyGraph.setShowLegend(false);
//		xyGraph.setShowTitle(false);
		CircularBufferDataProvider traceDataProvider = new CircularBufferDataProvider(false);
		traceDataProvider.setBufferSize(100);
		
		if(account!=null) {
			double dayCreated = (double)(account.getCreationDate().getTime() / (1000 * 60 * 60 * 24));
			double dayToday = (double)(new Date().getTime() / (1000 * 60 * 60 * 24));
			double day;
			
			// processed transactions in reverse order
			List<Transaction> transactions = account.getTransactions().stream().filter(t -> t.isProcessed())
					.sorted((t1, t2) -> t2.getProcessedDate().compareTo(t1.getProcessedDate()))
					.collect(Collectors.toList());
			
			double currentAmount = account.getBalance();
			double maxDay = dayToday - dayCreated;
			double minDay = 0;
			
			// first item, today's balance
			traceDataProvider.addSample(new Sample(maxDay, currentAmount));
			
			for(Transaction t: transactions) {
				day = (double)(t.getProcessedDate().getTime() / (1000*60*60*24)) - dayCreated;
				boolean incomingTransfer = !t.getSourceAccount().equals(account);
				currentAmount = (incomingTransfer) ? t.getRecipientAccountDeltaAmount() : t.getSourceAccountDeltaAmount();
				//
				traceDataProvider.addSample(new Sample(day, currentAmount));
			}
			// last item: today
			traceDataProvider.addSample(new Sample(minDay, currentAmount));
		}
		
		
//        traceDataProvider.addSample(new Sample(10,11));
//        traceDataProvider.addSample(new Sample(23,44));
//        traceDataProvider.addSample(new Sample(34,55));
//        traceDataProvider.addSample(new Sample(45.0d,45));
//        traceDataProvider.addSample(new Sample(56.0d,88));
//        traceDataProvider.addSample(new Sample(78.0d,98));
//        traceDataProvider.addSample(new Sample(88.0d,52));
//        traceDataProvider.addSample(new Sample(99.0d,23));
//        traceDataProvider.setCurrentXDataArray(new double[] { 10, 23, 34, 45, 56, 78, 88, 99 });
//        traceDataProvider.setCurrentYDataArray(new double[] { 11, 44, 55, 45, 88, 98, 52, 23 });
 
        // create the trace
        Trace trace = new Trace("Trace1-XY Plot", xyGraph.primaryXAxis, xyGraph.primaryYAxis, traceDataProvider);
 
        // set trace property
        trace.setPointStyle(PointStyle.XCROSS);
 
        // add the trace to xyGraph
        xyGraph.addTrace(trace);
        
	}
	
	public static CircularBufferDataProvider dataProvider(Account account) {
		CircularBufferDataProvider provider = new CircularBufferDataProvider(false);
		provider.setBufferSize(100);
		if (account != null) {
			double dayToday = (new Date().getTime() / (1000 * 60 * 60 * 24));
			double dayCreated = (account.getCreationDate().getTime() / (1000 * 60 * 60 * 24));
			double dayTransaction;
			//
			// processed transactions in reverse order
			List<Transaction> transactions = account.getTransactions().stream()
					.filter(t->t.isProcessed())
					.sorted((t1,t2)->t2.getProcessedDate().compareTo(t1.getProcessedDate()))
					.collect(Collectors.toList());
			//
			double currentAmount = account.getBalance();
			double maxDay = dayToday - dayCreated;
			double minDay = 0;
			// first item, today's balance
			provider.addSample(new Sample(maxDay, currentAmount));
			for (Transaction t : transactions) {
				// transaction time in days until today
				dayTransaction = (double) ((t.getProcessedDate().getTime() / (1000 * 60 * 60 * 24))-dayCreated);
				// change in the amount due to the transaction
				boolean incomingTransfer = !t.getSourceAccount().equals(account);
				currentAmount -= (incomingTransfer) ? t.getRecipientAccountDeltaAmount() : t.getSourceAccountDeltaAmount();
				// 
				provider.addSample(new Sample(dayTransaction, currentAmount));
			}
			// add last item: today
			provider.addSample(new Sample(minDay, currentAmount));
		}
		return provider;
	}

}
