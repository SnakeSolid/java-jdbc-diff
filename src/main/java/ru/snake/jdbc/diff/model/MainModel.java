package ru.snake.jdbc.diff.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyledDocument;

import ru.snake.jdbc.diff.document.SqlDocument;
import ru.snake.jdbc.diff.listener.DocumentModifiedListener;
import ru.snake.jdbc.diff.model.listener.ComparedDatasetListener;
import ru.snake.jdbc.diff.model.listener.ConnectionListener;
import ru.snake.jdbc.diff.model.listener.EditorStateListener;

/**
 * Main frame internal state model. Contains connections settings and text
 * documents from query and result data set.
 *
 * @author snake
 *
 */
public class MainModel {

	private final SqlDocument queryDocument;

	private final List<ComparedDataset> comparedDatasets;

	private final List<ConnectionListener> connectionListeners;

	private final List<EditorStateListener> editorStateListeners;

	private final List<ComparedDatasetListener> comparedDatasetListeners;

	private ConnectionSettings leftConnection;

	private ConnectionSettings rightConnection;

	private boolean modified;

	private File file;

	/**
	 * Creates empty model instance with given configuration settings.
	 */
	public MainModel() {
		this.queryDocument = new SqlDocument();
		this.comparedDatasets = new ArrayList<>();
		this.connectionListeners = new ArrayList<>();
		this.editorStateListeners = new ArrayList<>();
		this.comparedDatasetListeners = new ArrayList<>();
		this.leftConnection = null;
		this.rightConnection = null;
		this.modified = false;
		this.file = null;

		this.queryDocument.addDocumentListener(new DocumentModifiedListener(this::setModified));
	}

	/**
	 * Set modified state for model.
	 *
	 * @param document
	 *            changed document
	 */
	private void setModified(final Document document) {
		if (document == queryDocument) {
			this.modified = true;

			fireEditorStateChanged();
		}
	}

	/**
	 * Returns true if query document in this model was modified, otherwise
	 * returns false.
	 *
	 * @return true if model modified
	 */
	public boolean isModified() {
		return modified;
	}

	/**
	 * Returns currently opened file. Can return {@code null} if file is new.
	 *
	 * @return opened file name
	 */
	public File getFile() {
		return file;
	}

	/**
	 * Returns true if this model contains query loaded from file or already
	 * saved query.
	 *
	 * @return true if query has associated file
	 */
	public boolean hasFile() {
		return file != null;
	}

	/**
	 * Returns {@link Document} associated with query in current model.
	 *
	 * @return query document
	 */
	public StyledDocument getQueryDocument() {
		return queryDocument;
	}

	/**
	 * Returns list of {@link ComparedDataset} compared during query execution.
	 *
	 * @return data set document
	 */
	public List<ComparedDataset> getComparedDatasets() {
		return comparedDatasets;
	}

	/**
	 * Set connection settings and fire connection changed event to all
	 * listeners.
	 *
	 * @param aLeftConnection
	 *            left connection
	 * @param aRightConnection
	 *            right connection
	 */
	public void setConnections(final ConnectionSettings aLeftConnection, final ConnectionSettings aRightConnection) {
		this.leftConnection = aLeftConnection;
		this.rightConnection = aRightConnection;

		fireConnectionChanged();
	}

	/**
	 * Returns left connection setting. If connection settings not selected
	 * returns {@code null}.
	 *
	 * @return left connection settings
	 */
	public ConnectionSettings getLeftConnection() {
		return leftConnection;
	}

	/**
	 * Returns right connection setting. If connection settings not selected
	 * returns {@code null}.
	 *
	 * @return right connection settings
	 */
	public ConnectionSettings getRightConnection() {
		return rightConnection;
	}

	/**
	 * Adds new connection changed listener. Listener will be called when
	 * connection will be changed.
	 *
	 * @param listener
	 *            listener
	 */
	public void addConnectionListener(final ConnectionListener listener) {
		this.connectionListeners.add(listener);
	}

	/**
	 * Removes given connection changed listener from model.
	 *
	 * @param listener
	 *            listener
	 */
	public void removeConnectionListener(final ConnectionListener listener) {
		this.connectionListeners.remove(listener);
	}

	/**
	 * Fire connection changed event to all connection listeners.
	 */
	private void fireConnectionChanged() {
		for (ConnectionListener listener : this.connectionListeners) {
			listener.connectionsChanged(this, this.leftConnection, this.rightConnection);
		}
	}

	/**
	 * Add another editor state listener. Listener will be called on next event.
	 *
	 * @param listener
	 *            listener
	 */
	public void addEditorStateListener(final EditorStateListener listener) {
		this.editorStateListeners.add(listener);
	}

	/**
	 * Remove listener from internal listener list.
	 *
	 * @param listener
	 *            listener
	 */
	public void removeEditorStateListener(final EditorStateListener listener) {
		this.editorStateListeners.remove(listener);
	}

	/**
	 * Fire editor state changed event to all listeners.
	 */
	private void fireEditorStateChanged() {
		for (EditorStateListener listener : this.editorStateListeners) {
			listener.editorStateChanged(this, modified, file);
		}
	}

	/**
	 * Adds new compared dataset listener. Listener will be called when datasets
	 * changed.
	 *
	 * @param listener
	 *            listener
	 */
	public void addComparedDatasetListener(final ComparedDatasetListener listener) {
		comparedDatasetListeners.add(listener);
	}

	/**
	 * Removes given compared dataset listener from model.
	 *
	 * @param listener
	 *            listener
	 */
	public void removeComparedDatasetListener(final ComparedDatasetListener listener) {
		comparedDatasetListeners.remove(listener);
	}

	/**
	 * Fire compared dataset clean event for all connection listeners.
	 */
	private void fireDatasetsCleared() {
		for (ComparedDatasetListener listener : comparedDatasetListeners) {
			listener.comparedDatasetsCleared(this);
		}
	}

	/**
	 * Fire compared dataset push event for all connection listeners.
	 *
	 * @param dataset
	 *            pushed dataset
	 */
	private void fireDatasetPushed(final ComparedDataset dataset) {
		for (ComparedDatasetListener listener : comparedDatasetListeners) {
			listener.comparedDatasetPushed(this, dataset);
		}
	}

	/**
	 * Clear all compared datasets and fire clear event for all listeners.
	 * listeners.
	 */
	public void cleanComparedDatasets() {
		comparedDatasets.clear();

		fireDatasetsCleared();
	}

	/**
	 * Push dataset to compared dataset list and fire push event for all
	 * listeners.
	 *
	 * @param dataset
	 *            dataset to add
	 */
	public void pushComparedDataset(final ComparedDataset dataset) {
		comparedDatasets.add(dataset);

		fireDatasetPushed(dataset);
	}

	/**
	 * Returns unmodifiable list of currently compared connections.
	 *
	 * @return compared datasets
	 */
	public List<ComparedDatasetListener> getComparedDatasetListeners() {
		return Collections.unmodifiableList(comparedDatasetListeners);
	}

	/**
	 * Reset query area content and current file. Fires editor changed event to
	 * all listeners.
	 *
	 * @throws BadLocationException
	 *             if error occurred
	 */
	public void setQueryStateNew() throws BadLocationException {
		int length = queryDocument.getLength();

		if (length > 0) {
			queryDocument.remove(0, length);
		}

		modified = false;
		file = null;

		fireEditorStateChanged();
	}

	/**
	 * Set current file name from query. Fires editor changed event to all
	 * listeners.
	 *
	 * @param savedFile
	 *            save file
	 */
	public void setQueryStateSaved(final File savedFile) {
		modified = false;
		file = savedFile;

		fireEditorStateChanged();
	}

	/**
	 * Set current file name and query editor content. Fires editor changed
	 * event to all listeners.
	 *
	 * @param loadedFile
	 *            loaded file
	 * @param text
	 *            query text
	 * @throws BadLocationException
	 *             if error occurred
	 */
	public void setQueryStateLoaded(final File loadedFile, final String text) throws BadLocationException {
		AttributeSet attributes = SimpleAttributeSet.EMPTY;
		int length = queryDocument.getLength();

		if (length > 0) {
			queryDocument.remove(0, length);
		}

		queryDocument.insertString(0, text, attributes);
		modified = false;
		file = loadedFile;

		fireEditorStateChanged();
	}

}
