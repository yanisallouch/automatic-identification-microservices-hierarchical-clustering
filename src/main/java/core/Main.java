package core;


/**
 * Step 1: in this step, the hierarchical clustering algorithm, which produces
 * a binary tree also called dendrogram, is applied on the classes of the OO
 * application.
 * 
 * Step 2: this step aims to obtain disjoint clusters from the dendrogram. Each
 * cluster is considered as a microservice.
 */

public class Main {

	// Building a Dendrogram from OO Source Code
	// Partitioning a Dendrogram to Obtain Microservices
	
	/**
	 * Algorithm 1: Hierarchal clustering
	 * input : OO source code code
	 * output: A dendrogram dendro
	 * 
	 * let Sclasses be the set of classes extracted from the OO source code code;
	 * Let SClusters be a set of clusters of classes;
	 * for each class ∈ Sclasses do
	 * 	let class be a cluster;
	 * 	add cluster to SClusters;
	 * end
	 * while size(SClusters) > 1 do
	 * 	let (cluster1, cluster2) be the closest clusters based on the quality function;
	 * 	Let Newcluster ← merge(cluster1, cluster2);
	 * 	remove cluster1 and cluster2 from SClusters;
	 * 	add Newcluster to SClusters;
	 * end
	 * dendr = getCluster(SClusters);
	 * return dendro;
	 */
	

}