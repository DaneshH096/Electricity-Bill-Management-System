<?php 
    // if (isset($_SESSION['user'])) 
    // {
    //     $user = $_SESSION['user'];
    // }
    // else
    //     $user = "noname error";
    
?>
<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="navbar-header">
        <a class="navbar-brand" href="index.php"><i class="fa fa-bolt"></i> E-Bill</a>
    </div>
    <!-- /.navbar-header -->
    
    <a style="position:absolute;top:30px;left:-1px;font-size:20px;color:#7f7f7f;padding:5px;background-color: #000;border-bottom-right-radius:10px;text-decoration:none; background:transparent; " href="#menu-toggle" id="menu-toggle" <i class="fa fa-dedent"></i></a>

    <ul class="nav navbar-right top-nav">
        <li class="dropdown">
            <?php 
                echo "<a href=\"#\" class=\"dropdown-toggle\" data-toggle=\"dropdown\"><i class=\"fa fa-user\"></i> ADMIN <b class=\"caret\"></b></a> ";
             ?>
            <ul class="dropdown-menu">
                <li>
                    <a href="logout.php"><i class="fa fa-fw fa-power-off"></i> Log Out</a>
                </li>
            </ul>
        </li>
    </ul>
</nav>