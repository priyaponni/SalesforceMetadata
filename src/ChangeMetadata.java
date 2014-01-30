package com.salesforce;



public class ChangeMetadata {
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		if(args.length < 4){
			System.err.println("Usage : <folderName(folder directly containing apex files)> <packageName> <majorVersion/minorVersion> <newVersionNumber>");
			return;
		}
		
		// TODO Auto-generated method stub
		MetadataTransformer transformer = new MetadataTransformer(args[0],args[1],args[2],args[3]);
		transformer.processMetaFiles();
	}

}
