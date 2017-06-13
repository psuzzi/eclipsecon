package com.asegno.e4.banking.parts;

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

import com.asegno.e4.banking.model.Account;
import com.asegno.e4.banking.model.Transaction;

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
		Trace trace = new Trace("Trace1", xyGraph.getPrimaryXAxis(), xyGraph.getPrimaryYAxis(), dataProvider(account));
		trace.setPointStyle(PointStyle.CIRCLE);
		trace.setLineWidth(2);
		// add the trace to xyGraph
		xyGraph.addTrace(trace);
		xyGraph.setShowLegend(false);
		xyGraph.setShowTitle(false);
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
