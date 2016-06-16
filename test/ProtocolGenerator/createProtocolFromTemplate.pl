#!perl

#createProtocolFromTemplate("ProtocolTemplate.tml", "HowTo", "");
#createProtocolFromTemplate("ProtocolTemplate.tml", "HowTo", "Knowledge");
true;

sub createProtocolFromTemplate {

  open FILE, $_[0] or die $!;

  $b = join("",<FILE>);

  $b =~ s/%ClassName%/$_[1]/mg;
  
  if ($_[2] ne '')
  {
    $b =~ s/%SuperClassName%/extends $_[2]Protocol/mg;
  }
  else
  {
    $b =~ s/%SuperClassName%//mg;
  }

  open OUTPUT, ">target\\$_[1]Protocol.scala" or die $!; 
  print OUTPUT $b; 
  close OUTPUT;
  #print $b."\n";
}