package lecture.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.collections.FXCollections;
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
import javafx.stage.Stage;
import lecture.jdbc.vo.BookVO;

public class BookSearchJavaFX extends Application {
	// tableview 안에 데이터를 표현할떄 VO를 가져다가 한 줄씩 표현
		// 어떤 VO를 사용하는지 class이름을 generic으로 지정
		
		TableView<BookVO> tableView;
		TextField textField;
		Button deleteBtn;
		String deleteISBN;
		
		// 생성자 ; JDBC 준비단계
		public BookSearchJavaFX() {
			
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
			
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} 
		}// 
		
		public void start(Stage primaryStage) throws Exception {
				
			// 1. layout - BorderPane(동서남북중앙-5개영역)
			BorderPane root = new BorderPane();
			root.setPrefSize(700, 500);
			
			// 2. BorderPane - FlowPane
			FlowPane flowpane = new FlowPane();
			flowpane.setPadding(new Insets(10,10,10,10)); // 여백
	        flowpane.setColumnHalignment(HPos.CENTER); // 정렬
	        flowpane.setPrefSize(700, 40); // 크기
	        flowpane.setHgap(10); // 버튼 사이 간격
			
	        // 3. component
	        textField = new TextField();
	        textField.setPrefSize(250, 40);
	        
	        textField.setOnAction(e-> {
	        	
	        	String jdbc_url = "jdbc:mysql://127.0.0.1:3306/library?characterEncoding=UTF-8&serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true";
	    		String id = "root";
	    		String pw = "test1234";
	    		
	    		Connection con = null;
				try {
					con = DriverManager.getConnection(jdbc_url,id,pw);
				} catch (SQLException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
	    		
	        	StringBuffer sql = new StringBuffer();
				sql.append("SELECT bisbn, btitle, bauthor, bprice ");
				sql.append("FROM book ");
				sql.append("WHERE btitle like ? ");
				sql.append("ORDER BY bprice DESC");
				
				try {
				PreparedStatement pstmt = con.prepareStatement(sql.toString());
				
				pstmt.setString(1, "%"+textField.getText()+"%");
				
				ResultSet rs = pstmt.executeQuery();
				
				ObservableList<BookVO> list = FXCollections.observableArrayList();
				
				while(rs.next()) {
					BookVO book = new BookVO(rs.getString("bisbn"),rs.getString("btitle"),rs.getString("bauthor"),rs.getInt("bprice"));
			        list.add(book);
				} 
				tableView.setItems(list);
				rs.close();
				pstmt.close();
				con.close();
				
				} catch (Exception e1) {
					e1.printStackTrace();
				}
	        });
	    
	        // delete button
	        
	        deleteBtn = new Button("선택된 책 삭제");
	        deleteBtn.setPrefSize(150, 40);
	        deleteBtn.setDisable(true); // 현재는 사용할 수 없는 상태
	        deleteBtn.setOnAction(e->{
	        	
	        });
	        
	        
	        flowpane.getChildren().add(textField);
	        flowpane.getChildren().add(deleteBtn);
	        
	        // 4. tableview(정렬기능있음) - column 객체 생성
	        // TableColumn<VO class, data type>, ("컬럼명")
	        TableColumn<BookVO, String> isbnColumn = new TableColumn<>("ISBN");
	        isbnColumn.setMinWidth(150); // column minimum width
	        // 해당 컬럼의 데이터는 VO의 어떤 field에서 값을 가져올지 설정
	        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("bisbn"));
	        
	        TableColumn<BookVO, String> titleColumn = new TableColumn<>("TITLE");
	        titleColumn.setMinWidth(150);
	        titleColumn.setCellValueFactory(new PropertyValueFactory<>("btitle"));
	        
	        TableColumn<BookVO, String> authorColumn = new TableColumn<>("AUTHOR");
	        authorColumn.setMinWidth(150);
	        authorColumn.setCellValueFactory(new PropertyValueFactory<>("bauthor"));
	        
	        TableColumn<BookVO, Integer> priceColumn = new TableColumn<>("PRICE");
	        priceColumn.setMinWidth(150);
	        priceColumn.setCellValueFactory(new PropertyValueFactory<>("bprice"));
	        
	        // 5. tableview - data 생성 ; ArrayList와 유사한 list 사용
	        // ObservableList ; javaFX가 제공해주는 static method
	        
	        tableView = new TableView<BookVO>();
	       
	        // 6. column 추가 
	        tableView.getColumns().addAll(isbnColumn,titleColumn,authorColumn,priceColumn);
	        
	        // tableview 각 행들에 대해 특정 이벤트 설정
	        tableView.setRowFactory( e-> {
	        	// table row를 만들어서 해당 행에 이벤트 처리를 한 후 해당 행을 리턴하는 방식
	        	TableRow<BookVO> row = new TableRow<>();
	        	row.setOnMouseClicked(e1-> {
	        		deleteBtn.setDisable(false); //삭제 버튼 활성화
	        		BookVO book = row.getItem(); // 내가 클릭한 행 확인
	        		//삭제할 책의 key인 bisbn을 버튼이 클릭되었을 때 확인
	        		String jdbc_url = "jdbc:mysql://127.0.0.1:3306/library?characterEncoding=UTF-8&serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true";
	        		String id = "root";
	        		String pw = "test1234";
	        		
	        		Connection con = null;
					try {
						con = DriverManager.getConnection(jdbc_url,id,pw);
					} catch (SQLException e3) {
						// TODO Auto-generated catch block
						e3.printStackTrace();
					}
	        		
	        		deleteISBN = book.getBisbn();
	        		String sql = "DELETE FROM book WHERE bisbn = ?";
	        		try {
						con.setAutoCommit(false); // transaction
						
						PreparedStatement pstmt = con.prepareStatement(sql);
						pstmt.setString(1, deleteISBN);
						int count = pstmt.executeUpdate();
						
						// transation ; 결과 처리
						if (count == 1) {
							con.commit();
							// 자동으로 refresh ??
							
						} else {
							con.rollback();
						}
						
						pstmt.close();
						con.close();
					} catch (SQLException e2) {
						e2.printStackTrace();
					}
	        		
	        	});
	        	return row;
	        });
	        
	        root.setCenter(tableView);
	        root.setBottom(flowpane);
	        
	        Scene scene = new Scene(root);
	        primaryStage.setScene(scene);
	        
	        primaryStage.setTitle("Simple JavaFX TableView");
	        
//	        primaryStage.setOnCloseRequest(e->{
//	        	// 창을 닫을 때 이벤트를 잡아서 처리 
//	        	try {
//					con.close();
//				} catch (SQLException e1) {
//					e1.printStackTrace();
//				}
//	        });
	        
	        primaryStage.show();  
	       
	                
		}

		public static void main(String[] args) {
		
			launch();
			
		}
}
