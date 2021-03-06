import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.AuthorizeSecurityGroupIngressRequest;
import com.amazonaws.services.ec2.model.CreateKeyPairRequest;
import com.amazonaws.services.ec2.model.CreateKeyPairResult;
import com.amazonaws.services.ec2.model.CreateSecurityGroupRequest;
import com.amazonaws.services.ec2.model.CreateSecurityGroupResult;
import com.amazonaws.services.ec2.model.IpPermission;
import com.amazonaws.services.ec2.model.KeyPair;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;

public class CreateEC2 {
	
	public static void main(String[] args) throws Exception {
		//Credentials
		AWSCredentials credentials = new PropertiesCredentials(AwsConsoleApp.class.getResourceAsStream("AwsCredentials.properties"));
		AmazonEC2 ec2 = new AmazonEC2Client(credentials);
	
		CreateSecurityGroupRequest createSecurityGroupRequest = new CreateSecurityGroupRequest();
		createSecurityGroupRequest.withGroupName("JavaSecurityGroup").withDescription("My Java Security Group");
	
		CreateSecurityGroupResult createSecurityGroupResult = ec2.createSecurityGroup(createSecurityGroupRequest);
	
		IpPermission ipPermission = new IpPermission();
		ipPermission.withIpRanges("111.111.111.111/32", "150.150.150.150/32")
		.withIpProtocol("tcp")
		.withFromPort(22)
		.withToPort(22);
	
		AuthorizeSecurityGroupIngressRequest authorizeSecurityGroupIngressRequest = new AuthorizeSecurityGroupIngressRequest();
		
		authorizeSecurityGroupIngressRequest.withGroupName("JavaSecurityGroup").withIpPermissions(ipPermission);
		
		ec2.authorizeSecurityGroupIngress(authorizeSecurityGroupIngressRequest);
		
		CreateKeyPairRequest createKeyPairRequest = new CreateKeyPairRequest();
	
		String keyname = "JavaTest";
		createKeyPairRequest.withKeyName(keyname);
		
		CreateKeyPairResult createKeyPairResult = ec2.createKeyPair(createKeyPairRequest);
		
		
		KeyPair keyPair = new KeyPair();
	
		keyPair = createKeyPairResult.getKeyPair();
	
		String privateKey = keyPair.getKeyMaterial();
		
		System.out.println(privateKey);
		
		RunInstancesRequest runInstancesRequest = new RunInstancesRequest();
		runInstancesRequest.withImageId("ami-08842d60")
		.withInstanceType("t2.micro")
		.withMinCount(1)
		.withMaxCount(1)
		.withKeyName(keyname)
		.withSecurityGroups("JavaSecurityGroup");
		
		RunInstancesResult runInstancesResult = ec2.runInstances(runInstancesRequest);
	}

}
