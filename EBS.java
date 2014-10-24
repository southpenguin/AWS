import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.AttachVolumeRequest;
import com.amazonaws.services.ec2.model.CreateVolumeRequest;
import com.amazonaws.services.ec2.model.CreateVolumeResult;



public class EBS {
	
    static AmazonEC2 ec2;
	public static void main(String[] args) throws Exception {

		try {
			AWSCredentials credentials = new PropertiesCredentials(
		   			 AwsSample.class.getResourceAsStream("AwsCredentials.properties"));
			AmazonEC2Client ec2 = new AmazonEC2Client(credentials);
				
			CreateVolumeRequest createVolumeRequest = new CreateVolumeRequest()
		   	    .withAvailabilityZone("instanceZone")			//Zone of my current instance running on
		   	    .withSize(1); 			//Size of the volume, unit GigaByte

			CreateVolumeResult createVolumeResult = ec2.createVolume(createVolumeRequest);

			AttachVolumeRequest attachRequest = new AttachVolumeRequest()
		   	    .withVolumeId(createVolumeResult.getVolume().getVolumeId())
		   	    .withInstanceId("instanceID")	//This is my running instance ID
		   	    .withDevice("/dev/sdf");		//Regular volume type

			Thread.sleep(3000);			//Need to wait for volume to initialize

			ec2.attachVolume(attachRequest);
			
        } catch (AmazonServiceException ase) {
                System.out.println("Caught Exception: " + ase.getMessage());
                System.out.println("Reponse Status Code: " + ase.getStatusCode());
                System.out.println("Error Code: " + ase.getErrorCode());
                System.out.println("Request ID: " + ase.getRequestId());
        } 
    }
}
