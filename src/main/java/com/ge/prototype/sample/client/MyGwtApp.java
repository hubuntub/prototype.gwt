package com.ge.prototype.sample.client;

import java.util.LinkedHashMap;
import java.util.Map;

import org.moxieapps.gwt.highcharts.client.Chart;
import org.moxieapps.gwt.highcharts.client.ChartSubtitle;
import org.moxieapps.gwt.highcharts.client.ChartTitle;
import org.moxieapps.gwt.highcharts.client.Point;
import org.moxieapps.gwt.highcharts.client.Series;
import org.moxieapps.gwt.highcharts.client.ToolTip;
import org.moxieapps.gwt.highcharts.client.ToolTipData;
import org.moxieapps.gwt.highcharts.client.ToolTipFormatter;
import org.moxieapps.gwt.highcharts.client.labels.DataLabels;
import org.moxieapps.gwt.highcharts.client.plotOptions.PiePlotOptions;
import org.moxieapps.gwt.uploader.client.File;
import org.moxieapps.gwt.uploader.client.Uploader;
import org.moxieapps.gwt.uploader.client.events.FileDialogCompleteEvent;
import org.moxieapps.gwt.uploader.client.events.FileDialogCompleteHandler;
import org.moxieapps.gwt.uploader.client.events.FileDialogStartEvent;
import org.moxieapps.gwt.uploader.client.events.FileDialogStartHandler;
import org.moxieapps.gwt.uploader.client.events.FileQueueErrorEvent;
import org.moxieapps.gwt.uploader.client.events.FileQueueErrorHandler;
import org.moxieapps.gwt.uploader.client.events.FileQueuedEvent;
import org.moxieapps.gwt.uploader.client.events.FileQueuedHandler;
import org.moxieapps.gwt.uploader.client.events.UploadCompleteEvent;
import org.moxieapps.gwt.uploader.client.events.UploadCompleteHandler;
import org.moxieapps.gwt.uploader.client.events.UploadErrorEvent;
import org.moxieapps.gwt.uploader.client.events.UploadErrorHandler;
import org.moxieapps.gwt.uploader.client.events.UploadProgressEvent;
import org.moxieapps.gwt.uploader.client.events.UploadProgressHandler;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DragLeaveEvent;
import com.google.gwt.event.dom.client.DragLeaveHandler;
import com.google.gwt.event.dom.client.DragOverEvent;
import com.google.gwt.event.dom.client.DragOverHandler;
import com.google.gwt.event.dom.client.DropEvent;
import com.google.gwt.event.dom.client.DropHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.widgetideas.client.ProgressBar;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class MyGwtApp implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";


	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		final VerticalPanel progressBarPanel = new VerticalPanel();
		final Map<String, ProgressBar> progressBars = new LinkedHashMap<String, ProgressBar>();
		final Map<String, Image> cancelButtons = new LinkedHashMap<String, Image>();
		
		
		final Chart chart = new Chart().setType(Series.Type.PIE).setMarginRight(0);
	
		chart.setToolTip(new ToolTip().setFormatter(new ToolTipFormatter() {
			public String format(ToolTipData toolTipData) {
				return "<b>" + toolTipData.getSeriesName() + "</b><br/>"
						+ toolTipData.getPointName() + ": "
						+ toolTipData.getYAsDouble() + " %";
			}
		}));

		ChartTitle chartTitle = new ChartTitle();
		chartTitle.setText("Progress");
		ChartSubtitle subTitle = new ChartSubtitle();
		subTitle.setText(null);
		chart.setTitle(chartTitle, subTitle);
		chart.setPlotBackgroundColor("none");
		chart.setPlotBorderWidth(0);
		chart.setPlotShadow(false);
		chart.setTitle("Progress");
		chart.setWidth("200px");
		chart.setHeight("200px");
		
		
		final Series localSeries = chart.createSeries();
		localSeries.setPlotOptions(new PiePlotOptions().setSize(.50)
				.setInnerSize(.20)
				.setDataLabels(new DataLabels().setEnabled(false)));
		
		final Series globalSeries = chart.createSeries();
		globalSeries.setPlotOptions(new PiePlotOptions().setSize(.45)
				.setInnerSize(.20)
				.setDataLabels(new DataLabels().setEnabled(false)));
		
		chart.addSeries(localSeries, true, true);
		chart.addSeries(globalSeries, true, true);
		final Uploader uploader = new Uploader();
		uploader.setUploadURL("/FileServletUpload")
				.setButtonWidth(133)
				.setButtonHeight(22)
				.setFileSizeLimit("50 MB")
				.setAjaxUploadEnabled(true)
				.setButtonCursor(Uploader.Cursor.HAND)
				.setButtonAction(Uploader.ButtonAction.SELECT_FILES)
				.setFileQueuedHandler(new FileQueuedHandler() {
					public boolean onFileQueued(
							final FileQueuedEvent fileQueuedEvent) {
						// Create a Progress Bar for this file
						final ProgressBar progressBar = new ProgressBar(0.0,
								1.0, 0.0, new CancelProgressBarTextFormatter());
						progressBar.setTitle(fileQueuedEvent.getFile()
								.getName());
						progressBar.setHeight("18px");
						progressBar.setWidth("200px");
						progressBars.put(fileQueuedEvent.getFile().getId(),
								progressBar);
						// Add Cancel Button Image
						final Image cancelButton = new Image("/images/cancel.png");
						cancelButton.setStyleName("cancelButton");
						cancelButton.addClickHandler(new ClickHandler() {
							public void onClick(ClickEvent event) {
								uploader.cancelUpload(fileQueuedEvent.getFile()
										.getId(), false);
								progressBars.get(
										fileQueuedEvent.getFile().getId())
										.setProgress(-1.0d);
								cancelButton.removeFromParent();
							}
						});
						cancelButtons.put(fileQueuedEvent.getFile().getId(),
								cancelButton);

						
						
						
						// Add the Bar and Button to the interface
						HorizontalPanel progressBarAndButtonPanel = new HorizontalPanel();
						progressBarAndButtonPanel.add(progressBar);
						progressBarAndButtonPanel.add(cancelButton);
						progressBarPanel.add(progressBarAndButtonPanel);
						return true;
					}
				})
				.setUploadProgressHandler(new UploadProgressHandler() {
					public boolean onUploadProgress(
							
							UploadProgressEvent uploadProgressEvent) {
						File file = uploadProgressEvent.getFile();
						String fileName = file.getName();
						ProgressBar progressBar = progressBars
								.get(file.getId());
						double value = (double) uploadProgressEvent
								.getBytesComplete()
								/ uploadProgressEvent.getBytesTotal();
						progressBar.setProgress(value);
						localSeries.setPoints(
								new Point[] { new Point(fileName, value)
										.setColor("#FFBC75") });
					
						return true;
					}
				})
				.setUploadCompleteHandler(new UploadCompleteHandler() {
					public boolean onUploadComplete(
							UploadCompleteEvent uploadCompleteEvent) {
						cancelButtons
								.get(uploadCompleteEvent.getFile().getId())
								.removeFromParent();
						int value = cancelButtons.size();
						globalSeries.setPoints(
							new Point[] { new Point("files", value)
									.setColor("#95CEFF") });
						uploader.startUpload();
						return true;
					}
				})
				.setFileDialogStartHandler(new FileDialogStartHandler() {
					public boolean onFileDialogStartEvent(
							FileDialogStartEvent fileDialogStartEvent) {
						if (uploader.getStats().getUploadsInProgress() <= 0) {
							// Clear the uploads that have completed, if none
							// are in process
							progressBarPanel.clear();
							progressBars.clear();
							cancelButtons.clear();
							
						}
						return true;
					}
				})
				.setFileDialogCompleteHandler(new FileDialogCompleteHandler() {
					public boolean onFileDialogComplete(
							FileDialogCompleteEvent fileDialogCompleteEvent) {
						if (fileDialogCompleteEvent.getTotalFilesInQueue() > 0) {
						
							if (uploader.getStats().getUploadsInProgress() <= 0) {
								uploader.startUpload();
							}
						}
						return true;
					}
				}).setFileQueueErrorHandler(new FileQueueErrorHandler() {
					public boolean onFileQueueError(
							FileQueueErrorEvent fileQueueErrorEvent) {
						Window.alert("Upload of file "
								+ fileQueueErrorEvent.getFile().getName()
								+ " failed due to ["
								+ fileQueueErrorEvent.getErrorCode().toString()
								+ "]: " + fileQueueErrorEvent.getMessage());
						return true;
					}
				}).setUploadErrorHandler(new UploadErrorHandler() {
					public boolean onUploadError(
							UploadErrorEvent uploadErrorEvent) {
						cancelButtons.get(uploadErrorEvent.getFile().getId())
								.removeFromParent();
						Window.alert("Upload of file "
								+ uploadErrorEvent.getFile().getName()
								+ " failed due to ["
								+ uploadErrorEvent.getErrorCode().toString()
								+ "]: " + uploadErrorEvent.getMessage());
						return true;
					}
				});
	
		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.add(uploader);
		if (Uploader.isAjaxUploadWithProgressEventsSupported()) {
			final Label dropFilesLabel = new Label("Drop Files Here");
			dropFilesLabel.setStyleName("dropFilesLabel");
			dropFilesLabel.addDragOverHandler(new DragOverHandler() {
				public void onDragOver(DragOverEvent event) {
					if (!uploader.getButtonDisabled()) {
						dropFilesLabel.addStyleName("dropFilesLabelHover");
					}
				}
			});
			dropFilesLabel.addDragLeaveHandler(new DragLeaveHandler() {
				public void onDragLeave(DragLeaveEvent event) {
					dropFilesLabel.removeStyleName("dropFilesLabelHover");
				}
			});
			dropFilesLabel.addDropHandler(new DropHandler() {
				public void onDrop(DropEvent event) {
					dropFilesLabel.removeStyleName("dropFilesLabelHover");

					if (uploader.getStats().getUploadsInProgress() <= 0) {
						progressBarPanel.clear();
						progressBars.clear();
						cancelButtons.clear();
					}

					uploader.addFilesToQueue(Uploader.getDroppedFiles(event
							.getNativeEvent()));
					event.preventDefault();
				}
			});
			verticalPanel.add(dropFilesLabel);
		}


		// noinspection GwtToHtmlReferences
		RootPanel.get("uploaderContainer").add(verticalPanel);
		RootPanel.get("progressContainer").add(progressBarPanel);
		RootPanel.get("chartContainer").add(chart);
	}

	protected class CancelProgressBarTextFormatter extends
			ProgressBar.TextFormatter {
		@Override
		protected String getText(ProgressBar bar, double curProgress) {
			if (curProgress < 0) {
				return "Cancelled";
			}
			return ((int) (100 * bar.getPercent())) + "%";
		}
	}
}
