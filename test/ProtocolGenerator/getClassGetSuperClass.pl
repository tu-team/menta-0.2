# Get two variables back
#my $classNameToProcess = "Knowledge.scala";
#my ($one, $two) = get_two($classNameToProcess);
#print "Name : $one\n";
#print "Super: $two\n";
true;

sub get_two {

my $lookfor = "class\s*(.*?){";
my $lookforTrait = "trait\s*(.*?){";
open FILE, $_[0] or die $!;
#print("Process $_[0] file\n");

while(<FILE>)
  {
  if(/$lookfor/ or /$lookforTrait/ )         
    {
	my $string = $1;
	#print ("$1\n");
	if($string  =~ m/.*?(\w+)\(??/) 
	{
		$className = $1;
		#print ("$1-\n");	
	}
	$superClassName = "";
	if($string  =~ /extends\s+?(\w+)\(??/) 
	{
		$superClassName = $1;
		#print ("$1- \n");	
	}
        last;
    }
  }

close(FILE); 

return ($className , $superClassName );
}
