package exam03;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.UnknownHostException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.print.Printer;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class Exam03_MultiEchoClient extends Application {
	
	TextArea textArea;
	TextField ipTextField;
	Button connBtn;
	TextField idTextField;
	TextField chatTextField;
	
	Socket socket;
	PrintWriter pr;
	BufferedReader br;
	
	private void printMsg(String msg) {
		Platform.runLater(()->{
			textArea.appendText(msg+"\n");
		});
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		BorderPane root = new BorderPane();
		root.setPrefSize(700, 500);
		
		textArea = new TextArea();
		root.setCenter(textArea);
		
		FlowPane upFlowPane = new FlowPane();
		upFlowPane.setPadding(new Insets(10,10,10,10));
		upFlowPane.setColumnHalignment(HPos.CENTER);
		upFlowPane.setPrefSize(700, 40);
		upFlowPane.setHgap(10);
		
		ipTextField = new TextField();
		ipTextField.setPrefSize(200, 40);
		
		connBtn = new Button("서버에 접속!!");
		connBtn.setPrefSize(150, 40);
		connBtn.setOnAction(e -> {
			try {
				socket = new Socket(ipTextField.getText(), 7777);
				pr = new PrintWriter(socket.getOutputStream());
				br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		
		upFlowPane.getChildren().add(ipTextField);
		upFlowPane.getChildren().add(connBtn);
						
		FlowPane bottomFlowPane = new FlowPane();
		bottomFlowPane.setPadding(new Insets(10,10,10,10)); // padding(여백)설정
		bottomFlowPane.setColumnHalignment(HPos.CENTER); // 정렬
		bottomFlowPane.setPrefSize(700, 40);
		bottomFlowPane.setHgap(10);	
		
		idTextField = new TextField();
		idTextField.setPrefSize(150, 40);
		
		chatTextField = new TextField();
		chatTextField.setPrefSize(300, 40);
		chatTextField.setOnAction(e-> {
			
			String id = idTextField.getText();
			String msg = chatTextField.getText();
			
			pr.println(id+">"+msg);
			pr.flush();
			
			try {
				String receive = br.readLine();
				printMsg(receive);
				
			} catch (IOException e2) {
				e2.printStackTrace();
			}
			
		});
		
		bottomFlowPane.getChildren().add(idTextField);
		bottomFlowPane.getChildren().add(chatTextField);
		
		
		root.setTop(upFlowPane);
		root.setBottom(bottomFlowPane);
		
		Scene scene = new Scene(root);
		
		primaryStage.setScene(scene);
		primaryStage.setTitle("Echo Client Program");
		primaryStage.show();
		
		
	}
	
public static void main(String[] args) {
	launch();
	
}
}
