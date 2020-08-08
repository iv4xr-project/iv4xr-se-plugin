/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package world;

import eu.iv4xr.framework.spatial.Vec3;
import eu.iv4xr.framework.spatial.meshes.Edge;
import eu.iv4xr.framework.spatial.meshes.Face;
import eu.iv4xr.framework.spatial.meshes.Mesh;
import helperclasses.datastructures.linq.QArrayList;


/**
 * This represents the raw navigation-mesh sent by Lab-Recruits. It is a list of
 * indices and and vertices, such that if indices[i] = k and vertices[k] is a
 * Vec3 p, then this means that the point p is a corner belonging to the face 
 * with the id/index k in the mesh.
 * 
 * The faces in the mesh are always triangles. And the indices are always
 * stored consecutively. So, for example indices[0], indices[1], indices[2]
 * belong to the first triangle, and then indices[3], indices[4], indices[5]
 * belong to the next triangle.
 * 
 * The triangles are numbered increasingly. So, indices[0]=indices[1]=indices[2] 
 * should be 0. The next triplets should point to index 1, and so on. 
 */
public class LabRecruitsRawNavMesh {

    public int[] indices;
    public Vec3[] vertices;

    public LabRecruitsRawNavMesh(int[] indices, Vec3[] vertices) {
        this.indices = indices;
        this.vertices = vertices;
    }
    
    /**
     * This will convert this raw-mesh into the mesh representation as wanted by
     * the iv4xr agents.See {@link eu.iv4xr.framework.spatial.meshes.Mesh}.
     */
    public Mesh covertToMesh() {
    	if (!new QArrayList<Vec3>(this.vertices).isDistinct())
            throw new IllegalArgumentException("There are duplicates in the vertex array!");
    	
    	Mesh mesh = new Mesh() ;

    	// (1) copy the vertices to the new-mesh:
        for (int i=0; i< vertices.length; i++) mesh.vertices.add(vertices[i]) ;
        
        // (2) constructing explicit triangles form this raw-mesh, and storing them in
        // the new mesh:
        
        // The triangles are not stored in tuples.
        // The indices of this raw-mesh have a format like: [t0v0, t0v1, t0v2, t1v0, t1v1, t1v2, .. ]
        // If T is a triangle with index tr, its vertices would be:
        //   vertices[tr*3], vertices[tr*3+1], vertices[tr*3+2]
        //
        int triangleCount = this.indices.length / 3;
        for (int tr=0; tr<triangleCount; tr++ ) {
        	int startSegment = tr*3 ;
        	var triangle = new Face(new int[3]) ;
        	// the indices of the corners can be found in indices[startSegment] ... indices[startSegment+2]
        	for (int k = 0 ; k < 3; k++) {
        		triangle.vertices[k] = indices[startSegment + k] ; 
        	}
        	mesh.faces.add(triangle) ;
        }
        
        // (3) and now the edges:
        
        for (int triangle = 0; triangle < triangleCount; triangle++) {
            int vstart = triangle * 3;

            // loop over the vertices that belong to this triangle
            // for a triangle this executes 3 times
            for (int from = vstart; from < vstart + 3; from++) {
                for (int to = from + 1; to < vstart + 3; to++) {

                    // Create the edge, and it to the new mesh if it is not a duplicate:
                    Edge e = new Edge(indices[from], indices[to]);
                    if (! mesh.edges.contains(e)) {
                    	mesh.edges.add(e) ;
                    }
                }
            }
        }
        
        return mesh ;
    }
    
}
