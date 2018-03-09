SUNFLOW v 0.07.3i
-----------------

This archive contains the SunFlow files modified for Sweet Home 3D, compared to the revision 396 of SunFlow repository available on SourceForge.net with the following command:
  svn co -r 396 https://sunflow.svn.sourceforge.net/svnroot/sunflow/trunk sunflow

Once you replaced repository source files by the files found in src folder, copy buildForSweetHome3D.xml to sunflow folder and build sunflow-0.07.3i.jar with the following ant command run in sunflow folder:
  ant -f buildForSweetHome3D.xml


Note: differences in source code are marked with comments starting by 
  // EP