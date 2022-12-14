package lecture.jdbc.view;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lecture.jdbc.controller.BookDoubleClickDialogController;
import lecture.jdbc.controller.BookSearchByKeywordController;
import lecture.jdbc.controller.DeleteByISBNController;
import lecture.jdbc.vo.BookVO;

// view : JavaFX
public class BookSearchMVC extends Application {

	TableView<BookVO> tableView;
	TextField textField;
	Button deleteBtn;

	String deleteISBN;
	String searchKeyword;
    String rowData;
	
	@Override
	public void start(Stage primaryStage) throws Exception {

		// 1. layout부터 설정해야 해요!
		// BorderPane을 이용할 꺼예요!(동서남북중앙)
		BorderPane root = new BorderPane();
		root.setPrefSize(700, 500);

		// 2. BorderPane 아래쪽에 붙일 판자(FlowPane)를 하나 생성, 속성 설정
		FlowPane flowpane = new FlowPane();
		flowpane.setPadding(new Insets(10, 10, 10, 10));
		flowpane.setColumnHalignment(HPos.CENTER);
		flowpane.setPrefSize(700, 40);
		flowpane.setHgap(10);

		// 3. 각각의 component를 생성해서 붙여요!
		textField = new TextField();
		textField.setPrefSize(250, 40);

		textField.setOnAction(e -> {
			// 이벤트 처리
			// controller를 이용해서 service에 로직 실행 요청
			// controller instance
			BookSearchByKeywordController controller = new BookSearchByKeywordController();

			// view 입장에서 : textField를 받아 observable list를 띄워야함
			ObservableList<BookVO> list = controller.getResult(textField.getText());
			tableView.setItems(list);
			searchKeyword = textField.getText();
			textField.clear();

		});

		// 삭제버튼도 만들어서 붙여요!
		deleteBtn = new Button("선택된 책 삭제");
		deleteBtn.setPrefSize(150, 40);
		deleteBtn.setDisable(true);
		deleteBtn.setOnAction(e -> {
			// 이벤트 처리
			// controller instance
			DeleteByISBNController controller = new DeleteByISBNController();
			ObservableList<BookVO> list = controller.getResult(deleteISBN, searchKeyword);
			tableView.setItems(list);

		});

		flowpane.getChildren().add(textField);
		flowpane.getChildren().add(deleteBtn);

		// 컬럼객체를 생성해요!
		// TableColumn<해당컬럼에 나오는 데이터를 어떤 VO에서 가져오지는 그 VO의 클래스를 명시,
		// VO에서 값을 가져올때 사용하는 데이터 타입>
		TableColumn<BookVO, String> isbnColumn = new TableColumn<>("ISBN"); // ISBN은 화면에 보여지는 컬럼의 이름
		isbnColumn.setMinWidth(150);
		// 해당컬럼의 데이터는 VO의 어떤 field에서 값을 가져올지를 설정!
		isbnColumn.setCellValueFactory(new PropertyValueFactory<>("bisbn"));

		TableColumn<BookVO, String> titleColumn = new TableColumn<>("TITLE"); // ISBN은 화면에 보여지는 컬럼의 이름
		titleColumn.setMinWidth(150);
		// 해당컬럼의 데이터는 VO의 어떤 field에서 값을 가져올지를 설정!
		titleColumn.setCellValueFactory(new PropertyValueFactory<>("btitle"));

		TableColumn<BookVO, String> authorColumn = new TableColumn<>("AUTHOR"); // ISBN은 화면에 보여지는 컬럼의 이름
		authorColumn.setMinWidth(150);
		// 해당컬럼의 데이터는 VO의 어떤 field에서 값을 가져올지를 설정!
		authorColumn.setCellValueFactory(new PropertyValueFactory<>("bauthor"));

		TableColumn<BookVO, Integer> priceColumn = new TableColumn<>("PRICE"); // ISBN은 화면에 보여지는 컬럼의 이름
		priceColumn.setMinWidth(150);
		// 해당컬럼의 데이터는 VO의 어떤 field에서 값을 가져올지를 설정!
		priceColumn.setCellValueFactory(new PropertyValueFactory<>("bprice"));

		// TableView에 표현할 데이터를 만들어 보아요!
		// TableView에 데이터를 밀어넣을때는.. ArrayList와 유사한 List를 사용

		tableView = new TableView<BookVO>();

		// 위에서 만들어진 컬럼객체를 TableView에 붙여요!
		tableView.getColumns().addAll(isbnColumn, titleColumn, authorColumn, priceColumn);

		// 나중에 TableView에 데이터가 표현될꺼예요!
		// 이때 각 행들에 대해서 이벤트를 설정할 수 있어요.
		// 정확하게 얘기하자면 각 행들에 대한 특정 설정을 할 수 있어요!
		tableView.setRowFactory(e -> {
			// TableRow(테이블의 각 행)을 만들어서
			TableRow<BookVO> row = new TableRow<>();
			// 해당 행에 이벤트 처리를 설정한 다음
			row.setOnMouseClicked(e1 -> {
				deleteBtn.setDisable(false); // 삭제버튼 활성화
				// 내가 어떤 행을 클릭했는지 확인을 해야 하니..
				BookVO book = row.getItem();
				// 삭제할 책의 ISBN을 버튼이 클리되었을때 알아내야 해요!
				deleteISBN = book.getBisbn();
				
				row.setOnMouseClicked(event -> {

					if (event.getClickCount() == 2 && (!row.isEmpty())) {
		
						String clickedRowData = book.getBisbn();

						BookDoubleClickDialogController controller = new BookDoubleClickDialogController();
						ObservableList<BookVO> list = controller.getResult(book.getBisbn());
						//tableView.setItems(list);
						
						
						TableView<BookVO> tableView1 = new TableView<BookVO>();
						
						TableColumn<BookVO, String> dateColumn = new TableColumn<>("DATE");
				        dateColumn.setMinWidth(150); // column minimum width
				        // 해당 컬럼의 데이터는 VO의 어떤 field에서 값을 가져올지 설정
				        dateColumn.setCellValueFactory(new PropertyValueFactory<>("bdate"));
				        
				        TableColumn<BookVO, Integer> pageColumn = new TableColumn<>("PAGE");
				        pageColumn.setMinWidth(150);
				        pageColumn.setCellValueFactory(new PropertyValueFactory<>("bpage"));
				        
				        TableColumn<BookVO, String> supplementColumn = new TableColumn<>("Supplement");
				        supplementColumn.setMinWidth(150);
				        supplementColumn.setCellValueFactory(new PropertyValueFactory<>("bsupplement"));
				        
				        TableColumn<BookVO, String> publisherColumn = new TableColumn<>("Publisher");
				        publisherColumn.setMinWidth(150);
				        publisherColumn.setCellValueFactory(new PropertyValueFactory<>("bpublisher"));
				        
				        

						// 위에서 만들어진 컬럼객체를 TableView에 붙여요!
						tableView1.getColumns().addAll(dateColumn, pageColumn, supplementColumn, publisherColumn);
						tableView1.setItems(list);
						Stage dialog = new Stage(StageStyle.UTILITY);
						dialog.initModality(Modality.WINDOW_MODAL);
						dialog.initOwner(primaryStage);
						
						dialog.setTitle("상세정보");
						
						BorderPane root1 = new BorderPane();
						Scene scene1 = new Scene(root1);
						root1.setPrefSize(700, 500);
						root1.setCenter(tableView1);
						dialog.setScene(scene1);
						dialog.show();

					}
				});
			});
			// 해당 행을 리턴하는 방식
			return row;
		});

//double click
		root.setCenter(tableView);
		root.setBottom(flowpane);

		Scene scene = new Scene(root);

		primaryStage.setScene(scene);
		primaryStage.setTitle("BookSearch JavaFX MVC");

		primaryStage.setOnCloseRequest(e -> {
			// 이벤트 처리
		});

		primaryStage.show();

	}

	public static void main(String[] args) {
		launch();
	}
}
