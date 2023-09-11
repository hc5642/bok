package sample;

import java.util.UUID;

public class UETRGenMain {

	public static void main(String[] args) {
		
		UUID uuid = UUID.randomUUID();
		String uuidStr = uuid.toString();
		System.out.println(uuidStr);

	}

}
