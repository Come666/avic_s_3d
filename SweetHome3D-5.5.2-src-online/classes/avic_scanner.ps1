& rm *.ply 
& rm *.pcd 

& rm  scan\*.ply
& rm  scan\*.png
& rm  scan\*.txt
& rm  scan\*.mtl
& rm  scan\*.obj

& .\common_scan.exe -r -et -ic -vs 2.0f

& .\common_scan_output.exe world.pcd -vs 2.0f

$cmd = ".\meshlab\meshlabserver.exe"

$paras = ""

$files = Get-ChildItem .\*.ply

foreach ($file in $files) 
{
	$paras += " -i " + $file
}

$paras += " -o .\scan\merge_mesh.ply -s .\mesh_smooth.mlx"

$Prms = $paras.Split(" ")

& "$cmd" $Prms
& cd .\scan
& ..\common_scan_texture.exe  .\merge_mesh.ply
& rm *.txt
& cd ..

